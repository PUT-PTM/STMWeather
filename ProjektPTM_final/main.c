#include "stm32f4xx_conf.h"
#include "stm32f4xx_gpio.h"
#include "stm32f4xx_rcc.h"
#include "stm32f4xx_tim.h"
#include "stm32f4xx_syscfg.h"
#include "stm32f4xx_exti.h"
#include "misc.h"
#include "stm32f4xx_adc.h"
#include "stm32f4xx_usart.h"
#include <stdio.h>

//*Bluetooth HC-06:
//* PC11 - linia TX
//* PC10 - linia RX

int DHT11_Temperature;
int DHT11_Humidity;

GPIO_InitTypeDef  GPIO_InitStructure;
TIM_TimeBaseInitTypeDef TIM_TimeBaseStructure;

void DHT11initTIM2(void)
{
	RCC_APB1PeriphClockCmd(RCC_APB1Periph_TIM2, ENABLE);

	TIM_TimeBaseStructure.TIM_Period = 84000000 - 1;
	TIM_TimeBaseStructure.TIM_Prescaler = 84;
	TIM_TimeBaseStructure.TIM_ClockDivision = 0;
	TIM_TimeBaseStructure.TIM_CounterMode = TIM_CounterMode_Up;
	TIM_TimeBaseInit(TIM2, &TIM_TimeBaseStructure);
	TIM_Cmd(TIM2, ENABLE);
}

void DHT11initGPIOAsOutput(void)
{
	RCC_AHB1PeriphClockCmd(RCC_AHB1Periph_GPIOA, ENABLE);

	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_1;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_OUT;
	GPIO_InitStructure.GPIO_OType = GPIO_OType_PP;
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_100MHz;
	GPIO_InitStructure.GPIO_PuPd = GPIO_PuPd_NOPULL;
	GPIO_Init(GPIOA, &GPIO_InitStructure);
}


void DHT11initGPIOAsInput(void)
{
	RCC_AHB1PeriphClockCmd(RCC_AHB1Periph_GPIOA, ENABLE);
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_1;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IN;
	GPIO_Init(GPIOA, &GPIO_InitStructure);
}


void DHT11_delay_us(int us)
{

	TIM2->CNT = 0;
	while ((TIM2->CNT) <= us);
}


int DHT11Read()
{
	u8 bits[5];
	u8 idx = 0;

	int i;
	for (i = 0; i< 5; i++) bits[i] = 0;

	DHT11initGPIOAsOutput();
	GPIO_ResetBits(GPIOA, GPIO_Pin_1);
	DHT11_delay_us(18000);
	GPIO_SetBits(GPIOA, GPIO_Pin_1);
	DHT11_delay_us(40);
	DHT11initGPIOAsInput();

	if (GPIO_ReadInputDataBit(GPIOA, GPIO_Pin_1)) return -1;
	DHT11_delay_us(80);

	if (!GPIO_ReadInputDataBit(GPIOA, GPIO_Pin_1)) return -1;
	DHT11_delay_us(80);

	for (i = 1; i <= 40; i++) //40 bajtów do odczytu
	{
		while (!GPIO_ReadInputDataBit(GPIOA, GPIO_Pin_1)); //Oczekiwanie na stan wysoki
		DHT11_delay_us(35);

		if (GPIO_ReadInputDataBit(GPIOA, GPIO_Pin_1)) //Je¿eli po 35us jest stan wysoki to logiczna jedynka
		{
			bits[idx] <<= 1;
			bits[idx] |= 1;

		}
		else  { bits[idx] <<= 1; } //Je¿eli nie logiczne zero

		if ((i % 8) == 0)   //Co 8 bitów zwiêkszenie licznika tablicy
		{
			idx++;
		}

		while (GPIO_ReadInputDataBit(GPIOA, GPIO_Pin_1)); //Oczekiwanie na stan niski
	}

	if (!(bits[0] + bits[1] + bits[2] + bits[3] == bits[4])) return -1; //Sprawdzenie sumy kontrolnej


	DHT11_Humidity = bits[0];

	DHT11_delay_us(35000);

	DHT11_Temperature = bits[2];

	return 0;
}

void config_diods() {
	RCC_AHB1PeriphClockCmd(RCC_AHB1Periph_GPIOD, ENABLE);
	/* Configure PD12, PD13, PD14 and PD15 in output pushpull mode */
	GPIO_InitTypeDef GPIO_InitStructure;
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_12 | GPIO_Pin_13 | GPIO_Pin_14
		| GPIO_Pin_15;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_OUT;
	GPIO_InitStructure.GPIO_OType = GPIO_OType_PP;
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_100MHz;
	GPIO_InitStructure.GPIO_PuPd = GPIO_PuPd_NOPULL;
	GPIO_Init(GPIOD, &GPIO_InitStructure);
}

void config_ADC2()
{
	GPIO_InitTypeDef GPIO_InitStructure;

	//inicjalizacja wejœcia ADC
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_4;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AN;
	GPIO_InitStructure.GPIO_PuPd = GPIO_PuPd_NOPULL;
	GPIO_Init(GPIOA, &GPIO_InitStructure);


	ADC_CommonInitTypeDef ADC_CommonInitStructure;
	// niezale¿ny tryb pracy przetworników
	ADC_CommonInitStructure.ADC_Mode = ADC_Mode_Independent;
	// zegar g³ówny podzielony przez 2
	ADC_CommonInitStructure.ADC_Prescaler = ADC_Prescaler_Div2;
	// opcja istotna tylko dla tryby multi ADC
	ADC_CommonInitStructure.ADC_DMAAccessMode = ADC_DMAAccessMode_Disabled;
	// czas przerwy pomiêdzy kolejnymi konwersjami
	ADC_CommonInitStructure.ADC_TwoSamplingDelay =
		ADC_TwoSamplingDelay_5Cycles;
	ADC_CommonInit(&ADC_CommonInitStructure);
	ADC_InitTypeDef ADC_InitStructure;
	//ustawienie rozdzielczoœci przetwornika na maksymaln¹ (12 bitów)
	ADC_InitStructure.ADC_Resolution = ADC_Resolution_12b;
	//wy³¹czenie trybu skanowania (odczytywaæ bêdziemy jedno wejœcie ADC Strona 4 z 6

	//w trybie skanowania automatycznie wykonywana jest konwersja na wielu
	//wejœciach/kana³ach)
	ADC_InitStructure.ADC_ScanConvMode = DISABLE;
	//w³¹czenie ci¹g³ego trybu pracy
	ADC_InitStructure.ADC_ContinuousConvMode = ENABLE;
	//wy³¹czenie zewnêtrznego wyzwalania
	//konwersja mo¿e byæ wyzwalana timerem, stanem wejœcia itd. (szczegó³y w
	//dokumentacji)
	ADC_InitStructure.ADC_ExternalTrigConv = ADC_ExternalTrigConv_T1_CC1;
	ADC_InitStructure.ADC_ExternalTrigConvEdge =
		ADC_ExternalTrigConvEdge_None;
	//wartoœæ binarna wyniku bêdzie podawana z wyrównaniem do prawej
	//funkcja do odczytu stanu przetwornika ADC zwraca wartoœæ 16-bitow¹
	//dla przyk³adu, wartoœæ 0xFF wyrównana w prawo to 0x00FF, w lewo 0x0FF0
	ADC_InitStructure.ADC_DataAlign = ADC_DataAlign_Right;
	//liczba konwersji równa 1, bo 1 kana³
	ADC_InitStructure.ADC_NbrOfConversion = 1;
	// zapisz wype³nion¹ strukturê do rejestrów przetwornika numer 1

	ADC_Init(ADC2, &ADC_InitStructure);
	ADC_RegularChannelConfig(ADC2, ADC_Channel_4, 1, ADC_SampleTime_84Cycles);
	ADC_Cmd(ADC2, ENABLE);
}

void config_ADC3()
{
	GPIO_InitTypeDef GPIO_InitStructure;

	//inicjalizacja wejœcia ADC
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_3;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AN;
	GPIO_InitStructure.GPIO_PuPd = GPIO_PuPd_NOPULL;
	GPIO_Init(GPIOA, &GPIO_InitStructure);


	ADC_CommonInitTypeDef ADC_CommonInitStructure;
	// niezale¿ny tryb pracy przetworników
	ADC_CommonInitStructure.ADC_Mode = ADC_Mode_Independent;
	// zegar g³ówny podzielony przez 2
	ADC_CommonInitStructure.ADC_Prescaler = ADC_Prescaler_Div2;
	// opcja istotna tylko dla tryby multi ADC
	ADC_CommonInitStructure.ADC_DMAAccessMode = ADC_DMAAccessMode_Disabled;
	// czas przerwy pomiêdzy kolejnymi konwersjami
	ADC_CommonInitStructure.ADC_TwoSamplingDelay =
		ADC_TwoSamplingDelay_5Cycles;
	ADC_CommonInit(&ADC_CommonInitStructure);
	ADC_InitTypeDef ADC_InitStructure;
	//ustawienie rozdzielczoœci przetwornika na maksymaln¹ (12 bitów)
	ADC_InitStructure.ADC_Resolution = ADC_Resolution_12b;
	//wy³¹czenie trybu skanowania (odczytywaæ bêdziemy jedno wejœcie ADC Strona 4 z 6

	//w trybie skanowania automatycznie wykonywana jest konwersja na wielu
	//wejœciach/kana³ach)
	ADC_InitStructure.ADC_ScanConvMode = DISABLE;
	//w³¹czenie ci¹g³ego trybu pracy
	ADC_InitStructure.ADC_ContinuousConvMode = ENABLE;
	//wy³¹czenie zewnêtrznego wyzwalania
	//konwersja mo¿e byæ wyzwalana timerem, stanem wejœcia itd. (szczegó³y w
	//dokumentacji)
	ADC_InitStructure.ADC_ExternalTrigConv = ADC_ExternalTrigConv_T1_CC1;
	ADC_InitStructure.ADC_ExternalTrigConvEdge =
		ADC_ExternalTrigConvEdge_None;
	//wartoœæ binarna wyniku bêdzie podawana z wyrównaniem do prawej
	//funkcja do odczytu stanu przetwornika ADC zwraca wartoœæ 16-bitow¹
	//dla przyk³adu, wartoœæ 0xFF wyrównana w prawo to 0x00FF, w lewo 0x0FF0
	ADC_InitStructure.ADC_DataAlign = ADC_DataAlign_Right;
	//liczba konwersji równa 1, bo 1 kana³
	ADC_InitStructure.ADC_NbrOfConversion = 1;
	// zapisz wype³nion¹ strukturê do rejestrów przetwornika numer 1

	ADC_Init(ADC3, &ADC_InitStructure);
	ADC_RegularChannelConfig(ADC3, ADC_Channel_3, 1, ADC_SampleTime_84Cycles);
	ADC_Cmd(ADC3, ENABLE);
}


void Zegar()
{
	RCC_AHB1PeriphClockCmd(RCC_AHB1Periph_GPIOA, ENABLE); //wej.ADC, wyj. DAC
	RCC_AHB1PeriphClockCmd(RCC_AHB1Periph_GPIOE, ENABLE);
	RCC_APB1PeriphClockCmd(RCC_APB1Periph_USART3, ENABLE);
	RCC_AHB1PeriphClockCmd(RCC_AHB1Periph_GPIOB, ENABLE);
	RCC_AHB1PeriphClockCmd(RCC_AHB1Periph_GPIOC, ENABLE);
	RCC_AHB1PeriphClockCmd(RCC_AHB1Periph_GPIOD, ENABLE);
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_ADC3, ENABLE); //ADC
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_ADC2, ENABLE); //ADC

}

//Funkcja konfigurujaca USART
void Config_USART()
{
	USART_InitTypeDef USART_InitStructure;
	USART_InitStructure.USART_BaudRate = 9600;
	USART_InitStructure.USART_WordLength = USART_WordLength_8b;
	USART_InitStructure.USART_StopBits = USART_StopBits_1;
	USART_InitStructure.USART_Parity = USART_Parity_No;
	USART_InitStructure.USART_HardwareFlowControl = USART_HardwareFlowControl_None;
	USART_InitStructure.USART_Mode = USART_Mode_Rx | USART_Mode_Tx;
	USART_Init(USART3, &USART_InitStructure);
}

//Funkcje konfigurujace piny TX i RX wykorzystywane (transmisja danych)
void Config_Tx()
{
	// konfiguracja linii Tx
	GPIO_PinAFConfig(GPIOC, GPIO_PinSource10, GPIO_AF_USART3);
	GPIO_InitTypeDef GPIO_InitStructure;
	GPIO_InitStructure.GPIO_OType = GPIO_OType_PP;
	GPIO_InitStructure.GPIO_PuPd = GPIO_PuPd_UP;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AF;
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_10;
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
	GPIO_Init(GPIOC, &GPIO_InitStructure);
}

void Config_Rx()
{
	// konfiguracja linii Rx
	GPIO_PinAFConfig(GPIOC, GPIO_PinSource11, GPIO_AF_USART3);
	GPIO_InitTypeDef GPIO_InitStructure;
	GPIO_InitStructure.GPIO_OType = GPIO_OType_PP;
	GPIO_InitStructure.GPIO_PuPd = GPIO_PuPd_UP;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AF;
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_11;
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
	GPIO_Init(GPIOC, &GPIO_InitStructure);
}
void nvic()
{
	//struktura do konfiguracji kontrolera NVIC
	NVIC_InitTypeDef NVIC_InitStructure;
	// wlaczenie przerwania zwi¹zanego z odebraniem danych (pozostale zrodla
	USART_ITConfig(USART3, USART_IT_RXNE, ENABLE);
	NVIC_InitStructure.NVIC_IRQChannel = USART3_IRQn;
	NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 0;
	NVIC_InitStructure.NVIC_IRQChannelSubPriority = 0;
	NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE;
	// konfiguracja kontrolera przerwan
	NVIC_Init(&NVIC_InitStructure);
	// wlaczenie przerwan od ukladu USART
	NVIC_EnableIRQ(USART3_IRQn);
}

//volatile char m[4] = {'1','2','3','4'};
//volatile uint8_t m[4] = { 73, 70, 66, 43 };
int ADC_Result2;
float napiecie2;
int ADC_Result3;
float napiecie3;
int deszcz, poradnia;

int ADC3_wartosc()
{
	if (ADC_Result3 == 4095) poradnia = 0; //ciemno
	else poradnia = 1; //jasno

	return poradnia;
}

int ADC2_wartosc()
{
	if (ADC_Result2<3000) deszcz = 1; //deszcz
	else deszcz = 0; //bez_deszczu

	return deszcz;
}


void USART3_IRQHandler(void)
{
	// sprawdzenie flagi zwiazanej z odebraniem danych przez USART
	if (USART_GetITStatus(USART3, USART_IT_RXNE) != RESET)
	{
		char komunikat = (char)USART3->DR;

		if (komunikat == '1')
		{
			GPIO_SetBits(GPIOD, GPIO_Pin_12 | GPIO_Pin_13 | GPIO_Pin_14 | GPIO_Pin_15);
		}
		else if (komunikat == '2')
		{
			while (USART_GetFlagStatus(USART3, USART_FLAG_TXE) == RESET);
			for (int x = 0; x<4; x++)
			{
				volatile uint8_t m[4] = { DHT11_Temperature, DHT11_Humidity, ADC3_wartosc(), ADC2_wartosc() };

				for (int zy = 0; zy<10000000; zy++);
				USART_SendData(USART3, m[x]);
				for (int y = 0; y<10000000; y++);
			}

			int i, j;
			for (i = 0; i<5000; i++)
				for (j = 0; j<5000; j++);
		}
		else
		{
			GPIO_SetBits(GPIOD, GPIO_Pin_14 | GPIO_Pin_15);
		}
	}
}




int main(void)
{

	SystemInit();
	Zegar();
	config_diods();
	config_ADC2();
	config_ADC3();
	DHT11initTIM2();
	nvic();
	Config_USART(); //konfiguracja USART
	Config_Tx(); // konfiguracja linii TX
	Config_Rx(); // konfiguracja linii RX
	USART_Cmd(USART3, ENABLE); // uruchomienie USART
	while (1)
	{
		// fragment odpowiedzialny za odczyt stanu
		ADC_SoftwareStartConv(ADC2);
		ADC_SoftwareStartConv(ADC3);
		while (ADC_GetFlagStatus(ADC3, ADC_FLAG_EOC) == RESET);
		ADC_Result3 = ADC_GetConversionValue(ADC3);

		napiecie3 = ADC_Result3 / 4096.0;
		napiecie3 = 5 * napiecie3;

		while (ADC_GetFlagStatus(ADC2, ADC_FLAG_EOC) == RESET);
		ADC_Result2 = ADC_GetConversionValue(ADC2);

		napiecie2 = ADC_Result2 / 4096.0;
		napiecie2 = 5 * napiecie2;

		DHT11Read();
		// koniec fragmentu odpowiedzialnego za odczyt stanu
	}
}
