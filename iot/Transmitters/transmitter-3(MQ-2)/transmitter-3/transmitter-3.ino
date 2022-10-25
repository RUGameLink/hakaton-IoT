#include <ESP8266WiFi.h>
#include <espnow.h>
uint8_t broadcastAddress[] = {0x8C, 0xAA, 0xB5, 0x59, 0x30, 0x31}; 
#define BOARD_ID 3     
typedef struct struct_message {
    int id;
    float x;
    float y;
    float z;
} struct_message;

struct_message myData;                                                    

unsigned long lastTime = 0;                                               
unsigned long timerDelay = 100;                                         

#include <TroykaMQ.h>

#define MQ2PIN A0
MQ2 mq2(MQ2PIN);

void OnDataSent(uint8_t *mac_addr, uint8_t sendStatus) {
  Serial.print("\r\ Delivery status: ");
  if (sendStatus == 0){ Serial.println("Delivery seccess"); }
  else{ Serial.println("Delivery fail"); }
}


void setup() {  
  Serial.begin(9600);                                                  
  WiFi.mode(WIFI_STA);                                                  
  WiFi.disconnect();

  mq2.calibrate();
  mq2.getRo();

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
    float propan= get_data_ppmpropan();

    Serial.print("propan=");
    Serial.print(propan);
    Serial.println(" ppm ");

    float methan= get_data_ppmmethan();

    Serial.print("methan=");
    Serial.print(methan);
    Serial.println(" ppm ");

    float smoke= get_data_ppmsmoke();

    Serial.print("smoke=");
    Serial.print(smoke);
    Serial.println(" ppm ");                                                                      
    myData.id = BOARD_ID;
    myData.x = propan;                                                        
    myData.y = methan;      
    myData.z = smoke;                                                    
    esp_now_send(0, (uint8_t *) &myData, sizeof(myData));                 
    lastTime = millis();
  }

}

float get_data_ppmpropan() {
float value=mq2.readLPG();
return value;
}

float get_data_ppmmethan() {
float value=mq2.readMethane();
return value;
}

float get_data_ppmsmoke() {
float value=mq2.readSmoke();
return value;
}
