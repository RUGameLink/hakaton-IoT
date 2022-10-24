int mq3;

void setup() {
   pinMode(A0, INPUT);    // пин для подключения сенсора
   analogWrite(A0, LOW);

   Serial.begin(9600);        // запускаем монитор порта
 }

void loop() {
   mq3 = analogRead(A0);   // считываем данные с порта A1

   Serial.print("Alcohol: ");
   Serial.println(mq3);          // выводим значение на монитор


   delay(500);
}
