#include <TroykaMQ.h>

#define MQ2PIN A0
MQ2 mq2(MQ2PIN);

void setup() {

Serial.begin(9600);
mq2.calibrate();
mq2.getRo();
}

void loop() {

int propan= get_data_ppmpropan();

Serial.print("propan=");
Serial.print(propan);
Serial.println(" ppm ");

int methan= get_data_ppmmethan();

Serial.print("methan=");
Serial.print(methan);
Serial.println(" ppm ");

int smoke= get_data_ppmsmoke();

Serial.print("smoke=");
Serial.print(smoke);
Serial.println(" ppm ");
}

int get_data_ppmpropan() {
int value=mq2.readLPG();
return value;
}

int get_data_ppmmethan() {
int value=mq2.readMethane();
return value;
}

int get_data_ppmsmoke() {
int value=mq2.readSmoke();
return value;
}
