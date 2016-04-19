/*
File: usart.c
Description: STM32 USART peripheral library
Author: Adam Orcholski, tath@o2.pl, www.tath.eu
Log (day/month/year):
- (07.04.2013) Initial
- (25.07.2015) Refactored
Note:
 - refer to official documentation to understand what is happening here
 - USART is set to transmit mode
*/

#include "usart.h"

#define CORE_FREQUENCY            72000000L
#define UART_BAUD_RATE_256000    CORE_FREQUENCY/256000L

/* usart init */
void USART_Init(USART_TypeDef * const pUsart)
{
    pUsart->BRR = UART_BAUD_RATE_256000;    /* set baudrate */
    pUsart->CR1 |= USART_CR1_TE; /* transmit mode enable*/
}

/* usart enable */
void USART_Enable(USART_TypeDef * const pUsart)
{
    pUsart->CR1 |= USART_CR1_UE;
}

/* usart disable */
void USART_Disable(USART_TypeDef * const pUsart)
{
    pUsart->CR1  &= ~USART_CR1_UE;
}

/* usart send single char */
void USART_SendChar(USART_TypeDef * const pUsart, char const input)
{
    while (!(pUsart->SR & USART_SR_TXE));
    pUsart->DR = (input & 0x1FF);
}

/* usart send string */
void USART_SendString(USART_TypeDef * const pUsart, const char * pInput)
{
    while (*pInput != '\0')
    {
        USART_SendChar(pUsart, *pInput);
        pInput ++;
    }
}
