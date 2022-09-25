package test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.Cuenta;

public class CuentaTest {
	private Cuenta unaCuenta;
	@BeforeEach
	public void setUp() throws Exception {
		unaCuenta=new Cuenta("06-456213-33","Alfredo Hernandez");
	}
	
	@Test
	public void testIngresarCuenta(){
		double saldoCuenta = unaCuenta.getSaldo();
		System.out.println("Saldo en cuenta= " + saldoCuenta);
		
		try {
			unaCuenta.ingresar(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		double saldoCuentaActual = unaCuenta.getSaldo();
		System.out.println("Saldo en cuenta actual = " + saldoCuentaActual);

	    assertTrue(saldoCuenta < saldoCuentaActual);
	    assertTrue(saldoCuentaActual==5000.00);
	}
	
	@Test
	public void testIngresarMontoNegativoLanzaExcepcion(){
		Exception exception = assertThrows(Exception.class, () -> { unaCuenta.retirar(-5000.00);});

	    String expectedMessage = "No se puede retirar una cantidad negativa";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.equals(expectedMessage));
	}
	
	@Test
	public void testRetirarConConceptoDeCuenta(){
		try {
			unaCuenta.ingresar(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		double saldoCuenta = unaCuenta.getSaldo();
		System.out.println("Saldo en cuenta = " + saldoCuenta);
		
		try {
			unaCuenta.retirar("Retiro parcial", 2500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		double saldoCuentaActual = unaCuenta.getSaldo();
		System.out.println("Saldo en cuenta actual = " + saldoCuentaActual);
		
	    assertTrue(saldoCuenta > saldoCuentaActual);
	    assertTrue(saldoCuentaActual==2500.00);
	}
}
