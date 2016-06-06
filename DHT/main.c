#include "stm32f4xx.h"
#include "defines.h"
#include "tm_stm32f4_delay.h"
#include "tm_stm32f4_am2301.h"
#include <stdio.h>


float temp;
float humi;

int main(void) {
    TM_AM2301_Data_t data;
    char str[50];
    char tab1[7];
    char tab2[7];
    /* Initialize system */
    SystemInit();

    /* Initialize delay */
    TM_DELAY_Init();

    /* Initialize sensor */
    TM_AM2301_Init();

    /* Reset time */
    TM_DELAY_SetTime(0);
    while (1) {
        /* Every 1 second */
        if (TM_DELAY_Time() > 1000) {
            /* Reset time */
            TM_DELAY_SetTime(0);

            /* Data valid */
            if (TM_AM2301_Read(&data) == TM_AM2301_OK) {
                /* Show on LCD */
            	//zmiana float na char
            	sprintf(tab1, "%f", (float)data.Temp/10);
            	sprintf(tab2, "%f", (float)data.Hum/10);

            	//temp = (float)data.Temp / 10;
            	//humi = (float)data.Hum / 10;
                //sprintf(str, "Humidity: %2.1f %%\nTemperature: %2.1f C", (float)data.Hum / 10, (float)data.Temp / 10);
            }
        }
        /* Do other stuff constantly */
    }
}
