#include <TroykaMQ.h>

#define PIN_MQ3 A0

MQ3 mq3(PIN_MQ3);

void setup()
{
// открываем последовательный порт
Serial.begin(9600);

mq3.calibrate();

}
void loop()
{
Serial.print(mq3.readAlcoholPpm());
Serial.println(" ppm ");
delay(2000);
}
