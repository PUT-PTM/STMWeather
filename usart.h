/*
File: usart.h
Description: STM32 USART peripheral library
Author: Adam Orcholski, tath@o2.pl, www.tath.eu
Log (day/month/year):
- (07.04.2013) Initial
- (19.07.2015) Refactored
Note
- before USART_Init you must configure output pin and RCC clock
  by yourself
- USART is set to transmit mode
Note:
- pUsart argument should be one of predefined values:
  USART1, USART2, USART3
*/

#ifndef _USART_DEF_
#define _USART_DEF_

#include "stm32f4xx.h"

/*
pGpio argument should be one of predefined values:
GPIOA, GPIOB, GPIOC, GPIOD, GPIOE, GPIOF, GPIOG
*/

/* Interface function declarations */
void USART_Init(USART_TypeDef * const pUsart);
void USART_Enable(USART_TypeDef * const pUsart);
void USART_Disable(USART_TypeDef * const pUsart);
void USART_SendChar(USART_TypeDef * const pUsart, const char input);
void USART_SendString(USART_TypeDef * const pUsart, const char * pInput);
#endif
