#include <ThingSpeak.h>
unsigned long myChannelNumber = 1603688;
const char * myWriteAPIKey = "B3M2P9A1MCSW8JC5";
#include <ESP8266WiFi.h>

char ssid[] = "ddd"; // your network SSID (name)
char pass[] = "1234567890"; // your network password
int keyIndex = 0; // your network key index number (needed only for WEP)
// контакт подключения аналогового выхода датчика
int analogMQ7 = A0;             // Пин к которому подключен A0
// контакты подключения светодоодов индикации
int val = 0;                    // Создаем переменную
WiFiClient client;


long lastUpdateTime = 0; // Переменная для хранения времени последнего считывания с датчика


void setup() {
Serial.begin(9600);
pinMode(analogMQ7, INPUT);
WiFi.mode(WIFI_STA);
ThingSpeak.begin(client);

WiFiConnect();
}

void loop() {

// Получение показателей с датчика
val = analogRead(analogMQ7);
Serial.print(val);
// Connect or reconnect to WiFi

// Measure Signal Strength (RSSI) of Wi-Fi connection
long rssi = WiFi.RSSI();

// Write value to Field 1 of a ThingSpeak Channel
int httpCode = ThingSpeak.writeField(myChannelNumber, 2, val, myWriteAPIKey);

if (httpCode == 200) {
Serial.println("Channel write successful.(Co)");
}
else {
Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (Co)");
}

//delay(1000);
}

void WiFiConnect(){
WiFi.begin("ddd", "1234567890");

  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println();

  Serial.print("Connected, IP address: ");
  Serial.println(WiFi.localIP());
}