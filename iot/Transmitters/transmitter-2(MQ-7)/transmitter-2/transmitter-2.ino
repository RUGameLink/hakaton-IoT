#include <ESP8266WiFi.h>
#include <espnow.h>
uint8_t broadcastAddress[] = {0x8C, 0xAA, 0xB5, 0x59, 0x30, 0x31};        


#define BOARD_ID 2                                                        

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
  if (sendStatus == 0){ Serial.println("Delivery seccess"); }
  else{ Serial.println("Delivery fail"); }
}

#include <TroykaMQ.h>

#define PIN_MQ7 A0

MQ7 mq7(PIN_MQ7);

void setup() {  
  Serial.begin(9600);                                                   
  WiFi.mode(WIFI_STA);    
  mq7.cycleHeat();                                            
  WiFi.disconnect();
  
  if (esp_now_init() != 0) {
    Serial.println("Error init ESP-NOW");
    return;
  } 
  
  esp_now_set_self_role(ESP_NOW_ROLE_CONTROLLER);  
  esp_now_register_send_cb(OnDataSent);                                   
  esp_now_add_peer(broadcastAddress, ESP_NOW_ROLE_SLAVE, 1, NULL, 0);    
}

void loop() {
  if ((millis() - lastTime) > timerDelay) {   
    mq7.calibrate();
    mq7.cycleHeat();
    float mq = mq7.readCarbonMonoxide();
    Serial.print(" CarbonMonoxide: ");
    Serial.print(mq);
    Serial.println(" ppm ");
    mq7.cycleHeat();                                                                                                                   
    myData.id = BOARD_ID;
    myData.x = mq;                                                         
    myData.y = 0.0;
    myData.z = 0.0;                                                    
    esp_now_send(0, (uint8_t *) &myData, sizeof(myData));                 
    lastTime = millis();
  }
}
