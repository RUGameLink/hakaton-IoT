#include <ESP8266WiFi.h>
#include <espnow.h>
#include <ThingSpeak.h>
unsigned long myChannelNumber = 1905663;
const char * myWriteAPIKey = "B3M2P9A1MCSW8JC5";
#include <ESP8266WiFi.h>
WiFiClient client;

// Structure example to receive data
// Must match the sender structure
typedef struct struct_message {
    int id;
    int x;
    int y;
} struct_message;

// Create a struct_message called myData
struct_message myData;

// Create a structure to hold the readings from each board
struct_message board1;
struct_message board2;
struct_message board3;
struct_message board4;

// Create an array with all the structures
struct_message boardsStruct[4] = {board1, board2, board3, board4};

// Callback function that will be executed when data is received
void OnDataRecv(uint8_t * mac_addr, uint8_t *incomingData, uint8_t len) {
  char macStr[18];
//  Serial.print("Packet received from: ");
//  snprintf(macStr, sizeof(macStr), "%02x:%02x:%02x:%02x:%02x:%02x",
//           mac_addr[0], mac_addr[1], mac_addr[2], mac_addr[3], mac_addr[4], mac_addr[5]);
//  Serial.println(macStr);
  memcpy(&myData, incomingData, sizeof(myData));
//  Serial.printf("Board ID %u: %u bytes\n", myData.id, len);
  // Update the structures with the new incoming data
  boardsStruct[myData.id-1].x = myData.x;
  boardsStruct[myData.id-1].y = myData.y;
//  Serial.printf("x value: %d \n", boardsStruct[myData.id-1].x);
//  Serial.printf("y value: %d \n", boardsStruct[myData.id-1].y);
//  Serial.println();
}
 
void setup() {
  // Initialize Serial Monitor
  Serial.begin(9600);
  
  // Set device as a Wi-Fi Station
  WiFi.mode(WIFI_STA);
  //WiFi.disconnect();

  // Init ESP-NOW
  if (esp_now_init() != 0) {
    Serial.println("Error initializing ESP-NOW");
    return;
  }
  ThingSpeak.begin(client);
  // Once ESPNow is successfully Init, we will register for recv CB to
  // get recv packer info
  esp_now_set_self_role(ESP_NOW_ROLE_SLAVE);
  esp_now_register_recv_cb(OnDataRecv);
}

void loop(){
  Serial.println("Board 1: ");
  // Передатчик - 1
  int board1X = boardsStruct[0].x;
  int board1Y = boardsStruct[0].y;
  Serial.print("Value 1: ");Serial.println(board1X);
  Serial.print("Value 2: ");Serial.print(board1Y);
  Serial.println();
  
  Serial.println("Board 2: ");
  // Передатчик - 2
  int board2X = boardsStruct[1].x;
  int board2Y = boardsStruct[1].y;
  Serial.print("Value 1: ");Serial.print(board2X);Serial.println("%");
  Serial.print("Value 2: ");Serial.print(board2Y);Serial.println("°C");
  Serial.println();
  int board3x = boardsStruct[2].x;
  Serial.println("Board 3");
  Serial.print("Value 1: ");Serial.print(board3x);
  Serial.println();
  int board4x = boardsStruct[3].x;
  Serial.println("Board 4");
  Serial.print("Value 1: ");Serial.print(board4x);
  Serial.println();
  if(board1X == 0 && board1Y == 0
  && board2X == 0
  && board3x == 0
  && board4x == 0){
    return;
  }
  WiFiConnect();
  delay(500);
  int httpCode = ThingSpeak.writeField(myChannelNumber, 3, board1Y, myWriteAPIKey);
  if (httpCode == 200) {
    Serial.println("Channel write successful.(temp)");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (temp)");
  } 
  delay(21000);
  //delay(20000);
  httpCode = ThingSpeak.writeField(myChannelNumber, 4,  board1X, myWriteAPIKey);
  if (httpCode == 200) {
    Serial.println("Channel write successful.(hum)");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (hum)");
  } 
  delay(21000);
  httpCode = ThingSpeak.writeField(myChannelNumber, 2,  board2X, myWriteAPIKey);
  if (httpCode == 200) {
    Serial.println("Channel write successful.(co)");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (co)");
  } 
  delay(21000);
  httpCode = ThingSpeak.writeField(myChannelNumber, 1,  board3x, myWriteAPIKey);
  if (httpCode == 200) {
    Serial.println("Channel write successful.(co2)");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (co2)");
  } 
  delay(21000);
  httpCode = ThingSpeak.writeField(myChannelNumber, 5,  board4x, myWriteAPIKey);
  if (httpCode == 200) {
    Serial.println("Channel write successful.(alco)");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode) + " (alco)");
  } 
  delay(21000);
  WiFi.disconnect();
  delay(1000);

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

