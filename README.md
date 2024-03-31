# ReadMe
Tüm kredinbizde mikroservisleri ve ilgili componentler, docker-compose.yml'da toplandı. Uygulamalar containerize edildi. 
`docker-compose up` komutuyla bu yapı kolayca başlatılabilir.

kredinbizde-service de loglar exception alındıgında kafka ya gönderilerek, log-service ile de kafka dan okunarak mongodb'ye kaydedildi.

ecommerce-microservices : Sipariş oluşturulduktan sonra asenkron yapı ile müşteriye mail olarak bilgi gönderilmesi sağlandı.
Sipariş oluşturulduktan sonra asenkron yapı ile fatura olusturuldu.
FeignErrorDecoder class'ı olusturularak diğer microservice'lerde alınan exception ve exception mesajların client microservice de yakalanması sağlandı.

[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/b5ww4GXt)
# 3.Hafta Ödevi

1. Aşağıdaki kavramları örneklerle açıklayın. `(10 PUAN)`  
• Unit test  
• Integration test

2. Aşağıdaki özellikleri **kredinbizde** uygulamasına ekleyin. `(40 PUAN)`  
• Kredinbizde uygulaması için Garanti bankası için client oluşturun.  
• Kullanıcı Garanti bankası için kredi başvurusu yapabilmelidir.  
• Kullanıcı yapmış olduğu bütün başvuruları görüntüleyebilmelidir.  
• NotificationService için EMAIL,SMS,MOBILE_NOTIFICATION işlemlerini strateji pattern kullanarak implemantasyonu gerçekleştirin.  

3. Docker ile Kafka kurulumu gerçekleştirin ve exception alındığı durumlarda Kafka ile bütün loglar toplanmalı ve MongoDB üzerinde kaydedilmelidir. `(20 PUAN)`

4. Birinci ödevde yaptığınız müşteri, fatura, sipariş ve ürün ile oluşturduğunuz projenizi ayrı servisler olacak şekilde bölün ve aşağıdaki geliştirmeleri ekleyin. `(30 PUAN)`  
• GW ekleyin.  
• Discovery server ekleyin.  
• Fatura bilgilerinin asenkron oluşmasını sağlayan yapıyı kurun.  
• GlobalExceptionHandler ekleyin.  

---
*Eğitmen - Cem DIRMAN*  
*Kolay Gelsin*
