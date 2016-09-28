# STMWeather
##Overview##
STMWeather is a project of the weather station. It includes few sensors programmed with STM32F4VG Discovery Microcontroller and Android app. 
STMWeather can measure temperature and humidity (DHT11), rain presence(YL-83), light (LM393). 
All the data is send via bluetooth (HC-06) to the phone with Android system and app installed.
App receives the data, stores it in the database and displays it with appropriate icons.

##Tools##
 +<ul>
 +  <li>CooCox CoIDE</li>
 +  <li>STMStudio</li>
 +  <li>Realterm</li>
 +  <li>Android Studio</li>
 +</ul>
 
 +
##Pins connection ##
 
+HC-06   PC10 -> line RX 
+	       PC11 -> line TX 
+        VCC -> 3V
+	       GND -> GND 

+YL-83 	AO -> PA4 
+	      VCC -> 3V 
+	      GND -> GND 

+LM393  OUT -> PA2 
+	      VCC -> 5V 
+       GND -> GND 

+DHT11	Using breadboard (it needs to connect resistor 5km Ohm between VCC and DATA)
+	      DATA -> PA1
+	      VCC -> 3V
+	      GND -> GND

##License##
 +This project can be used under MIT license
 
 ##Credits##
 <ul>
 <li>Piotr Bochra</li>
 <li>Mateusz Nowak</li>
 <li>Supervisor: Michał Fularz</li>
 </ul>
