
#include "stm32f4xx_conf.h"
#include "stm32f4xx_gpio.h"
#include "stm32f4xx_rcc.h"
#include "stm32f4xx_tim.h"
#include "stm32f4xx_syscfg.h"
#include "stm32f4xx_exti.h"
#include "misc.h"
#include "stm32f4xx_adc.h"
#include "stm32f4xx_dac.h"

int ADC_Result;

float napiecie;

int main(void)
{


	RCC_AHB1PeriphClockCmd(RCC_AHB1Periph_GPIOD, ENABLE); //wej.ADC, wyj. DAC
	RCC_AHB1PeriphClockCmd(RCC_AHB1Periph_GPIOA, ENABLE); //wej.ADC, wyj. DAC
	RCC_APB1PeriphClockCmd(RCC_APB1Periph_DAC, ENABLE); //DAC
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_ADC1, ENABLE); //ADC

	GPIO_InitTypeDef GPIO_InitStructure;

	//inicjalizacja port�w diod LED
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_12 | GPIO_Pin_13 | GPIO_Pin_14 | GPIO_Pin_15;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_OUT;
	GPIO_InitStructure.GPIO_OType = GPIO_OType_PP;
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_100MHz;
	GPIO_InitStructure.GPIO_PuPd = GPIO_PuPd_NOPULL;
	GPIO_Init(GPIOD, &GPIO_InitStructure);
	//inicjalizacja wyj�cia DAC
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_4;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AN;
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_100MHz;
	GPIO_InitStructure.GPIO_PuPd = GPIO_PuPd_NOPULL;
	GPIO_Init(GPIOA, &GPIO_InitStructure);

	//inicjalizacja wej�cia ADC
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_1;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AN;
	GPIO_InitStructure.GPIO_PuPd = GPIO_PuPd_NOPULL;
	GPIO_Init(GPIOA, &GPIO_InitStructure);


	DAC_InitTypeDef DAC_InitStructure;
	DAC_InitStructure.DAC_Trigger = DAC_Trigger_None;
	DAC_InitStructure.DAC_WaveGeneration = DAC_WaveGeneration_None;
	DAC_InitStructure.DAC_LFSRUnmask_TriangleAmplitude = DAC_LFSRUnmask_Bit0;
	DAC_InitStructure.DAC_OutputBuffer = DAC_OutputBuffer_Enable;
	DAC_Init(DAC_Channel_1, &DAC_InitStructure);

	DAC_Cmd(DAC_Channel_1, ENABLE);


	ADC_CommonInitTypeDef ADC_CommonInitStructure;
	// niezale�ny tryb pracy przetwornik�w
	ADC_CommonInitStructure.ADC_Mode = ADC_Mode_Independent;
	// zegar g��wny podzielony przez 2
	ADC_CommonInitStructure.ADC_Prescaler = ADC_Prescaler_Div2;
	// opcja istotna tylko dla tryby multi ADC
	ADC_CommonInitStructure.ADC_DMAAccessMode = ADC_DMAAccessMode_Disabled;
	// czas przerwy pomi�dzy kolejnymi konwersjami
	ADC_CommonInitStructure.ADC_TwoSamplingDelay =
		ADC_TwoSamplingDelay_5Cycles;
	ADC_CommonInit(&ADC_CommonInitStructure);
	ADC_InitTypeDef ADC_InitStructure;
	//ustawienie rozdzielczo�ci przetwornika na maksymaln� (12 bit�w)
	ADC_InitStructure.ADC_Resolution = ADC_Resolution_12b;
	//wy��czenie trybu skanowania (odczytywa� b�dziemy jedno wej�cie ADC Strona 4 z 6

	//w trybie skanowania automatycznie wykonywana jest konwersja na wielu
	//wej�ciach/kana�ach)
	ADC_InitStructure.ADC_ScanConvMode = DISABLE;
	//w��czenie ci�g�ego trybu pracy
	ADC_InitStructure.ADC_ContinuousConvMode = ENABLE;
	//wy��czenie zewn�trznego wyzwalania
	//konwersja mo�e by� wyzwalana timerem, stanem wej�cia itd. (szczeg�y w
	//dokumentacji)
	ADC_InitStructure.ADC_ExternalTrigConv = ADC_ExternalTrigConv_T1_CC1;
	ADC_InitStructure.ADC_ExternalTrigConvEdge =
		ADC_ExternalTrigConvEdge_None;
	//warto�� binarna wyniku b�dzie podawana z wyr�wnaniem do prawej
	//funkcja do odczytu stanu przetwornika ADC zwraca warto�� 16-bitow�
	//dla przyk�adu, warto�� 0xFF wyr�wnana w prawo to 0x00FF, w lewo 0x0FF0
	ADC_InitStructure.ADC_DataAlign = ADC_DataAlign_Right;
	//liczba konwersji r�wna 1, bo 1 kana�
	ADC_InitStructure.ADC_NbrOfConversion = 1;
	// zapisz wype�nion� struktur� do rejestr�w przetwornika numer 1

	ADC_Init(ADC1, &ADC_InitStructure);
	ADC_RegularChannelConfig(ADC1, ADC_Channel_1, 1, ADC_SampleTime_84Cycles);
	ADC_Cmd(ADC1, ENABLE);

	int x = 1;
	while (1)
	{
		// fragment odpowiedzialny za odczyt stanu
		ADC_SoftwareStartConv(ADC1);
		while (ADC_GetFlagStatus(ADC1, ADC_FLAG_EOC) == RESET);
		ADC_Result = ADC_GetConversionValue(ADC1);

		napiecie = ADC_Result / 4096.0;
		napiecie = 5 * napiecie;
		// koniec fragmentu odpowiedzialnego za odczyt stanu
	}
}
