package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import modelo.Credito;
import modelo.Cuenta;

public class CreditoTest {
	private Date fecha;
	private Credito credito;
	private Cuenta unaCuenta;
	
	@BeforeEach
	public void setUp() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = "2020-09-15";
		try {
			fecha = sdf.parse(dateInString);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		credito = new Credito("06-456213-33","Alfredo Hernandez",fecha,5000.00);
		unaCuenta=new Cuenta("06-456213-33","Alfredo Hernandez");
	}

	@Test
	public void testComprasConTarjetaCredito(){
	
		assertNotNull(fecha);
		//Se crea una cuenta con $7000 de saldo
		try {
			unaCuenta.ingresar(7000.00);
			//se asigna una cuenta a una tarjeta de credito con tope de $5000
			credito.setCuenta(unaCuenta);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			// Compras con tarjeta de credito no modifican estado de cuenta hasta que se liquide
			credito.pagoEnEstablecimiento("MusiMundo",3000.00);
			credito.pagoEnEstablecimiento("Fravega",1000.00);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("cuenta saldo = " + unaCuenta.getSaldo());
		System.out.println("credito disponible =  " + credito.getCreditoDisponible());
		//saldo credito.saldo es lo que se viene gastando hasta el momento sin liquidar
		System.out.println("credito.getSaldo() " + credito.getSaldo());
		
		//automatizando los tests
		assertTrue(unaCuenta.getSaldo()==7000.00);
		assertTrue(credito.getCreditoDisponible()==1000.00);
		assertTrue(credito.getSaldo()==4000.00);
	}

	@Test
	public void testRetirarDineroSinCreditoDisponibleConTarjetaLanzaExcepcion(){
	
		assertNotNull(fecha);
		//Se crea una cuenta con $3000 de saldo
		try {
			unaCuenta.ingresar(3000.00);
			//se setea una cuenta a una tarjeta de credito que ya tiene 5000 de tope
			credito.setCuenta(unaCuenta);
			// Realizo un moviento mayor al tope de la tarjeta
			credito.pagoEnEstablecimiento("Coto",6000.00);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// Intento retirar  dinero en cajero, pero mi credito disponible es menor al
		// monto que busco retirar, por lo que deberia saltar una excepcion
		Exception exception = assertThrows(Exception.class, () -> {
			credito.retirar(5000.00);});

	    String expectedMessage = "Credito insuficiente";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.equals(expectedMessage));
	}
	
	@Test
	public void testIngresarConTarjetaCredito(){
	
		assertNotNull(fecha);
		try {
			//se asigna una cuenta a una tarjeta de credito con tope de $5000
			credito.setCuenta(unaCuenta);
			// Realizo compra con tarjeta de credito
			credito.pagoEnEstablecimiento("Megatone",3000.00);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.out.println("cuenta saldo = " + unaCuenta.getSaldo());
		//Lo que se viene gastando hasta el momento sin liquidar
		System.out.println("movimientos totales credito = " + credito.getSaldo());
		System.out.println("credito disponible =  " + credito.getCreditoDisponible());
		System.out.println("-------------------------------------------------------");

		try {
			credito.ingresar(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Saldos despues de ingresar:");
		System.out.println("cuenta saldo = " + unaCuenta.getSaldo());
		//Lo que se viene gastando hasta el momento sin liquidar
		System.out.println("movimientos totales credito = " + credito.getSaldo());
		System.out.println("credito disponible =  " + credito.getCreditoDisponible());
		
		//comprobamos que la cuenta tenga el saldo ingresado con la tarjeta de credito
		assertTrue(unaCuenta.getSaldo()==2000.00);
		assertTrue(credito.getCreditoDisponible()==0.00);
	}

}
