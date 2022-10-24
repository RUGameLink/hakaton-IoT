// Если плата ESP32
#ifdef ESP32
  #include <WiFi.h>
// Если плата ESP8266
#else
  #include <ESP8266WiFi.h>
#endif

void setup(){
  Serial.begin(115200);
  Serial.println(WiFi.macAddress());    // Определяем MAC адрес платы
}
 
void loop(){
  Serial.print("MAC адрес платы:  ");
  Serial.println(WiFi.macAddress());
  delay(5000);
}
