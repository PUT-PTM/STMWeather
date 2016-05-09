/*
File: main.c
Description: Example use of my DHT11 library
Author: Adam Orcholski, tath@o2.pl, www.tath.eu
Log (day/month/year):
- (07.04.2013) Initial
- (19.07.2015) Refactored
Note:
    - in this example i use GPIOA pin 1 to communicate with DHT11 and GPIOA pin9 as RX USART
    - some compilers does not support retargeting (usart through printf),
      so some changes might be requried in this case
*/

#include <stdio.h>     /* printf declaration */
#include <stm32f10x.h> /* Official STM register definitions */
#include <gpio.h>
#include <usart.h>
#include <dht11.h>

/* Entry point */
int main (void)
{
    uint8_t         dht11Data[5] = {0};     /* dht11 data container */
    volatile int    i = 0;                  /* Keil compiler doesn't like delay loops */

    /* Setup USART */
    RCC->APB2ENR |= RCC_APB2ENR_IOPAEN;     /* GPIOA clock enable (USART and DHT11) */
    RCC->APB2ENR |= RCC_APB2ENR_USART1EN;   /* USART1 clock enable */
    Gpio_SetPinAsOutput(GPIOA, GPIO_PIN9, GPIO_SPEED_50MHZ, GPIO_AF_PUSH_PULL);
    USART_Init(USART1);
    USART_Enable(USART1);
    
    /* setup DHT11 */
    DHT11_Init();

    printf("All initialization done\r\n");

    while(1)
    {
        if (DHT11_OK == DHT11_Read(dht11Data))
        {
            printf("DHT11 data: %d%%, %dC\r\n", dht11Data[0], dht11Data[2]);
        }
        else /* wrong checksum or timeout */
        {
            printf("DHT11: ERROR on read");
        }
        
        /* delay loop :D */
        for (i=0; i<2000000;i++);
    }
}
