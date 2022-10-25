#include <ESP8266WiFi.h>
#include <espnow.h>
#include <ThingSpeak.h>
unsigned long myChannelNumber = 1905663;
const char * myWriteAPIKey = "B3M2P9A1MCSW8JC5";
#include <ESP8266WiFi.h>
WiFiClient client;

typedef struct struct_message {
    int id;
    float x;
    float y;
    float z;
} struct_message;

struct_message myData;

struct_message board1;
struct_message board2;
struct_message board3;
struct_message board4;

struct_message boardsStruct[4] = {board1, board2, board3, board4};

void OnDataRecv(uint8_t * mac_addr, uint8_t *incomingData, uint8_t len) {
  char macStr[18];
  memcpy(&myData, incomingData, sizeof(myData));
  boardsStruct[myData.id-1].x = myData.x;
  boardsStruct[myData.id-1].y = myData.y;
  boardsStruct[myData.id-1].z = myData.z;
}
 
void setup() {
  Serial.begin(9600);
  
  WiFi.mode(WIFI_STA);

  if (esp_now_init() != 0) {
    Serial.println("Error initializing ESP-NOW");
    return;
  }
  ThingSpeak.begin(client);
  esp_now_set_self_role(ESP_NOW_ROLE_SLAVE);
  esp_now_register_recv_cb(OnDataRecv);
}

void loop(){
  // Передатчик - 1(DHT-11)
  Serial.println("Board 1: ");
  float board1X = boardsStruct[0].x;
  float board1Y = boardsStruct[0].y;
  Serial.print("Humditidy: ");Serial.println(board1X);
  Serial.print("Temp: ");Serial.print(board1Y);
  Serial.println();
  
    // Передатчик - 2(MQ-7)
  Serial.println("Board 2: ");
  float board2X = boardsStruct[1].x;
  Serial.print("CO: ");Serial.print(board2X);Serial.println();
  Serial.println();
    // Передатчик - 3(MQ-2)
  float board3x = boardsStruct[2].x;
  float board3y = boardsStruct[2].y;
  float board3z = boardsStruct[2].z;
  Serial.println("Board 3");
  Serial.print("propan: ");Serial.print(board3x);Serial.println();
  Serial.print("methan: ");Serial.print(board3y);Serial.println();
  Serial.print("smoke: ");Serial.print(board3z);Serial.println();
  Serial.println();
    // Передатчик - 4(MQ-3)
  float board4x = boardsStruct[3].x;
  Serial.println("Board 4");
  Serial.print("Alco: ");Serial.print(board4x);
  Serial.println();Serial.println();
  if(board1X == 0.0 && board1Y == 0.0
  && board2X == 0.0
  && board3x == 0.0
  && board4x == 0.0){
    return;
  }
  delay(5000);
  WiFiConnect();
  delay(500);
  // Пропан
  int httpCode = ThingSpeak.writeField(myChannelNumber, 1, board3x, myWriteAPIKey);
  if (httpCode == 200) {
    Serial.println("Channel write successful.(propan)");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (propan)");
  } 
  delay(21000);
  // CO
  httpCode = ThingSpeak.writeField(myChannelNumber, 2, board2X, myWriteAPIKey);
  if (httpCode == 200) {
    Serial.println("Channel write successful.(CO)");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (CO)");
  } 
  delay(21000);
  //Температура
  httpCode = ThingSpeak.writeField(myChannelNumber, 3, board1Y, myWriteAPIKey);
  if (httpCode == 200) {
    Serial.println("Channel write successful.(Temp)");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (Temp)");
  } 
  delay(21000);
  //Влажность
  httpCode = ThingSpeak.writeField(myChannelNumber, 4, board1X, myWriteAPIKey);
  if (httpCode == 200) {
    Serial.println("Channel write successful.(Humidity)");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (Humidity)");
  } 
  delay(21000);
  //Спирт
  httpCode = ThingSpeak.writeField(myChannelNumber, 5, board4x, myWriteAPIKey);
  if (httpCode == 200) {
    Serial.println("Channel write successful.(Alcohol)");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (Alcohol)");
  } 
  delay(21000);
  //Метан
  httpCode = ThingSpeak.writeField(myChannelNumber, 6, board3y, myWriteAPIKey);
  if (httpCode == 200) {
    Serial.println("Channel write successful.(methan)");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (methan)");
  } 
  delay(21000);
    //Smoke
  httpCode = ThingSpeak.writeField(myChannelNumber, 7, board3z, myWriteAPIKey);
  if (httpCode == 200) {
    Serial.println("Channel write successful.(smoke)");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (smoke)");
  } 
  delay(21000);
  WiFi.disconnect();
  delay(1000);
}
/*
1 - пропан (MQ-2)
2 - CO (MQ-7)
3 - температура
4 - влажность
5 - спирт (MQ-3)
6 - метан (MQ-2)
7 - smoke (MQ-2)
*/
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

