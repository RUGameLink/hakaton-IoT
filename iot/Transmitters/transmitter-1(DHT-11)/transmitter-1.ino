/*
  Rui Santos
  Complete project details at https://RandomNerdTutorials.com/esp-now-many-to-one-esp8266-nodemcu/ 
*/

/*
 MAC адрес платы:  48:3F:DA:54:F5:31 Главная(Приёмник)
 */



#include <ESP8266WiFi.h>
#include <espnow.h>
#include <DHT.h>      // подключаем библиотеку для датчика
DHT dht(5, DHT11); 
                                                          //  Куда подключен датчик ХОЛЛА 
int i,h;
uint8_t broadcastAddress[] = {0x8C, 0xAA, 0xB5, 0x59, 0x30, 0x31};        // МАС адрес получателя

#define BOARD_ID 1                                                        // Устанавливаем ID платы как 1

// структуры для отправки данных
typedef struct struct_message {
    int id;
    int x;
    int y;
} struct_message;


struct_message myData;                                                    // Создайте сообщение с именем myData для хранения отправляемых переменных

unsigned long lastTime = 0;                                               // Переменная для хранения времени
unsigned long timerDelay = 100;                                         // Задержка в 10 сек между отправками пакетов данных

// Callback-функция при отправке сообщения
void OnDataSent(uint8_t *mac_addr, uint8_t sendStatus) {
  Serial.print("\r\ Delivery status: ");
  if (sendStatus == 0){ 
    Serial.println("Delivery seccess"); 
    delay(500);
  }
  else{ Serial.println("Delivery fail"); }
}

void setup() {  
  Serial.begin(9600);   
  dht.begin();                                                   // Запускаем монитор порта на скорости 115200 бод
  WiFi.mode(WIFI_STA);                                                    // Режим работы Клиент
  WiFi.disconnect();
  
  // Инициализируем ESP-NOW
  if (esp_now_init() != 0) {
    Serial.println("Error init ESP-NOW");
    return;
  } 
  
  // Роль платы в ESP-NOW
  esp_now_set_self_role(ESP_NOW_ROLE_CONTROLLER);  
  esp_now_register_send_cb(OnDataSent);                                   // Получаем сообщение об отправке
  esp_now_add_peer(broadcastAddress, ESP_NOW_ROLE_SLAVE, 1, NULL, 0);     // Регистрируем пиры
}

void loop() {
  if ((millis() - lastTime) > timerDelay) {                               // Если прошло больше 10 секунд
  i = dht.readHumidity();                                          // Считываем освещённость
  h = dht.readTemperature();                                                // Считываем с датчика Холла
    myData.id = BOARD_ID;
    myData.x = i;                                                         // Отправляем данные
    myData.y = h;                                                         // Отправляем данные 
    esp_now_send(0, (uint8_t *) &myData, sizeof(myData));                 // Отправляем сообщение
    lastTime = millis();
  }
}
