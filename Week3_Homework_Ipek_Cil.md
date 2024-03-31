
# Unit Test - Integration Test

## Unit Test
Unit test, tanım olarak geliştirmiş olduğumuz projemizin en küçük parçasını test etmek anlamına gelir.
NOT: Java gibi OOP tabanlı programlama dillerinde bu parçalar metot olarak adlandırılır.
Yazılım bileşenlerinin (birimlerin) doğru çalışmasını sağlamak, sistemin tasarlandığı gibi çalıştığından emin olmak için yazılırlar. Unit testler 
1. **Kod kalitesini arttırır.**
2. **Otomatik çalıştırılır.**
3. **Hata ayıklama sürecini kolaylaştırır.**
4. **Kodun her değişiklik sonrasında hızlıca doğrulama yapılmasını sağlar.**
5. **Bütün sistemi değil sistemin parçalarını test etmektir. Bir kod bloğunu, bir metodu test ederler.**
6. **Unit test yazılan kodların hatalarını bulmak için kullanılmaz.**
7. **Unit test yazılan yerleri daha iyi analiz edebilir ve gerekli yerlerde refactoring yapma imkanı sağlar.**

### Unit Test Örnek
NOT: Eğer maven-quickstart bir proje oluşturduysak bize örnek olarak bir App ve AppTest oluşturacaktır.
Main → java altında kodlarımızı yazarken, test → java altında bu kodlara unit testler yazarız. 
Burada genelde isimlendirme olarak işi yapan class’ın isminin sonuna Test ifadesi eklenir(App ve AppTest).

```
public class CalculatorTest {

	@Test
	public void testSum() {
		// Given
		Calculator calculator = new Calculator();
		// When
		int result = calculator.sum(2, 2);
		// Then
		if (result != 4) {   // if 2 + 2 != 4
			Assert.fail();
		}
	}

	@Test
	public void testMinus() {
		Calculator calculator = new Calculator();
		Assert.assertEquals(0, calculator.minus(2, 2));
	}

	@Test
	public void testDivide() {
		Calculator calculator = new Calculator();
		Assert.assertEquals(2, calculator.divide(6, 3));
	}

	@Test(expected = ArithmeticException.class)
	public void testDivideWillThrowExceptionWhenDivideOnZero() {
		Calculator calculator = new Calculator();
		calculator.divide(6, 0);
	}
}
```
Bu kod bir JUnit test sınıfı olan CalculatorTest içerir. 
Bu sınıf, bir hesap makinesi uygulamasının test edilmesi için yazılmış testleri içerir.

İlk olarak, testSum() metodu, Calculator sınıfının sum() metodunu test eder. 
Bu metot, Calculator sınıfından bir örnek oluşturur, ardından sum() metodunu çağırarak 2 ve 2 sayılarını toplar ve sonucun 4 olduğunu kontrol eder. 
Eğer sonuç 4 değilse, Assert.fail() ile test başarısız olur.

İkinci olarak, testMinus() metodu, Calculator sınıfının minus() metodunu test eder. 
Bu metot, Calculator sınıfından bir örnek oluşturur, ardından minus() metodunu çağırarak 2 ve 2 sayılarını çıkarır ve sonucun 0 olduğunu doğrular.

Üçüncü olarak, testDivide() metodu, Calculator sınıfının divide() metodunu test eder. 
Bu metot, Calculator sınıfından bir örnek oluşturur, ardından divide() metodunu çağırarak 6'yı 3'e böler ve sonucun 2 olduğunu doğrular.

Son olarak, testDivideWillThrowExceptionWhenDivideOnZero() metodu, Calculator sınıfının divide() metodunun 0'a bölme durumunda bir ArithmeticException fırlatıp fırlatmadığını test eder. 
Bu metot, Calculator sınıfından bir örnek oluşturur, ardından divide() metodunu 6'yı 0'a bölmek için çağırır ve beklenen bir ArithmeticException oluşması gerektiğini belirtir.

Bu test sınıfı, Calculator sınıfının metodlarının doğru şekilde çalışıp çalışmadığını kontrol eder ve bu sayede kodun işlevsel doğruluğunu sağlar.

## Integration Test
- Entegrasyon testi, farklı modüllerin birbirine bağlandığı ve test edildiği yazılım testine yönelik bir yaklaşımdır. 
- Amaç, modüllerin birbirine bağlandığında amaçlandığı gibi çalışıp çalışmadığını görmektir. 
- Genellikle birim testinden sonra ve sistem testinden önce gerçekleştirilir.
Daha önce tüm bileşenleri test edilen sıfırdan bir bilgisayar oluşturduğumuzu düşünürsek,  bunları bir sisteme entegre ettiğinizde işe yaramayabilirler. Bunun nedeni, bileşenlerin bazı kusurları olması değil, birbirleriyle uyumlu olmamasıdır. Entegrasyon testi bu tür hataları tanımlamamıza yardımcı olur.

### Unit test ve Integration Test temel farklar
- Birim Testi, test edilebilir en küçük kod birimlerini test etmeyi amaçlarken Entegrasyon Testi, bir sistemin birden fazla bileşeninin etkileşimini test etmeye odaklanır**
- Genellikle birim testi, entegrasyon testinden önce gerçekleştirilir. Öncelikle bireysel birimlerin işlevsel olup olmadığını doğrulamamız gerekiyor. Ancak o zaman bunları daha büyük modüllere entegre edebilir ve ilişkilerini test edebiliriz

### Integration Test Örnek
```
public class PaymentIntegrationTest {

    @Test
    public void testSuccessfulPayment() {
        PaymentServiceMock paymentServiceMock = new PaymentServiceMock();

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addProduct(new Product("Ürün1", 100));

        PaymentProcessor paymentProcessor = new PaymentProcessor(paymentServiceMock);
        boolean paymentResult = paymentProcessor.processPayment(shoppingCart, "kullanıcı_adı", "şifre");

        assertTrue(paymentResult);
        assertTrue(shoppingCart.isPaid());
        assertEquals(0, shoppingCart.getProductCount());
    }

    @Test
    public void testFailedPayment() {
        PaymentServiceMock paymentServiceMock = new PaymentServiceMock(true);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addProduct(new Product("Ürün2", 200));

        PaymentProcessor paymentProcessor = new PaymentProcessor(paymentServiceMock);
        boolean paymentResult = paymentProcessor.processPayment(shoppingCart, "kullanıcı_adı", "şifre");

        assertTrue(!paymentResult);
        assertTrue(!shoppingCart.isPaid()); 
        assertEquals(1, shoppingCart.getProductCount());
    }

    private static class PaymentServiceMock implements PaymentService {
        private boolean simulateError;

        public PaymentServiceMock() {
            this(false);
        }

        public PaymentServiceMock(boolean simulateError) {
            this.simulateError = simulateError;
        }

        @Override
        public boolean processPayment(double amount, String username, String password) {
            if (!"kullanıcı_adı".equals(username) || !"şifre".equals(password)) {
                fail("Geçersiz kullanıcı adı veya şifre");
            }

            // Ödeme işlemi simülasyonu
            if (simulateError) {
                return false;
            } else {
                return true;
            }
        }
    }
}
```
Bu örnek, PaymentIntegrationTest adında bir test sınıfı içerir. İlk test (testSuccessfulPayment) başarılı bir ödeme senaryosunu simüle ederken, ikinci test (testFailedPayment) başarısız bir ödeme senaryosunu simüle eder. 
PaymentServiceMock sınıfı, ödeme işlemi servisinin sahte bir simülasyonunu sağlar. 
Bu şekilde gerçek bir ödeme işlemi gerçekleştirmeden önce entegrasyonun doğru çalışıp çalışmadığını test edebiliriz.


