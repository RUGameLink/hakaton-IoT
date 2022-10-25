#include <ESP8266WiFi.h>
#include <espnow.h>
#include <DHT.h>      
DHT dht(5, DHT11); 
uint8_t broadcastAddress[] = {0x8C, 0xAA, 0xB5, 0x59, 0x30, 0x31};        

#define BOARD_ID 1                                                       
typedef struct struct_message {
    int id;
    float x;
    float y;
    float z;
} struct_message;

struct_message myData;                                                    

unsigned long lastTime = 0;                                               
unsigned long timerDelay = 100;                                         

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
  dht.begin();                                                   
  WiFi.mode(WIFI_STA);                                                    
  WiFi.disconnect();

  if (esp_now_init() != 0) {
    Serial.println("Error init ESP-NOW");
    return;
  } 
  
  // Роль платы в ESP-NOW
  esp_now_set_self_role(ESP_NOW_ROLE_CONTROLLER);  
  esp_now_register_send_cb(OnDataSent);                                   
  esp_now_add_peer(broadcastAddress, ESP_NOW_ROLE_SLAVE, 1, NULL, 0);     
}

void loop() {
  if ((millis() - lastTime) > timerDelay) {                               
  float h = dht.readHumidity();                                          
  float t = dht.readTemperature();    
  Serial.print("Humidity: ");Serial.print(h);Serial.println();    
  Serial.print("Temp: ");Serial.print(t);Serial.println();                                        
    myData.id = BOARD_ID;
    myData.x = h;                                                         
    myData.y = t;
    myData.z = 0.0;                                                     
    esp_now_send(0, (uint8_t *) &myData, sizeof(myData));                 
    lastTime = millis();
  }
}
