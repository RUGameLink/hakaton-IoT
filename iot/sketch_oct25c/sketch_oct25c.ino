#include <TroykaMQ.h>

#define PIN_MQ7 A0

MQ7 mq7(PIN_MQ7);
 
void setup()
{
  Serial.begin(9600);
  mq7.cycleHeat();
}
 
void loop()
{
  mq7.calibrate();
  mq7.cycleHeat();
  Serial.print(" CarbonMonoxide: ");
  Serial.print(mq7.readCarbonMonoxide());
  Serial.println(" ppm ");
  delay(100);
  mq7.cycleHeat();
}