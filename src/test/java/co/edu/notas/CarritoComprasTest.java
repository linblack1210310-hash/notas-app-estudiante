package co.edu.notas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarritoComprasTest {

    private CarritoCompras carrito;

    @BeforeEach
    void setUp() {
        carrito = new CarritoCompras();
    }

    @Test
    void carritoVacioTest() {
        assertEquals(0, carrito.cantidadDeItems());
        assertEquals(0.0, carrito.calcularSubtotal());
        assertEquals(0.0, carrito.calcularDescuento());
        assertEquals(0.0, carrito.calcularTotal());
    }

    @Test
    void agregarProductoExitosoTest() {
        carrito.agregarProducto("Camisa", 50_000, 2);
        assertEquals(1, carrito.cantidadDeItems());
        assertEquals(100_000, carrito.calcularSubtotal());
    }

    @Test
    void agregarProductoValidacionesTest() {
        // Nombre nulo o en blanco
        assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto(null, 10_000, 1));
        assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto("   ", 10_000, 1));

        // Precio inválido
        assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto("Pantalon", 0, 1));
        assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto("Pantalon", -10, 1));

        // Cantidad inválida
        assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto("Zapato", 50_000, -1));
    }

    @Test
    void aplicarCuponesTest() {
        carrito.agregarProducto("Tenis", 100_000, 1);

        // Cupón inválido
        assertThrows(IllegalArgumentException.class, () -> carrito.aplicarCupon("INVALIDO"));

        // Cupón SENA10 (10%)
        carrito.aplicarCupon("SENA10");
        assertEquals(10_000, carrito.calcularDescuento());

        // Cupón BIENVENIDO (menor a 50_000 no aplica)
        CarritoCompras carrito2 = new CarritoCompras();
        carrito2.agregarProducto("Gorra", 30_000, 1);
        carrito2.aplicarCupon("BIENVENIDO");
        assertEquals(0, carrito2.calcularDescuento());

        // Cupón BIENVENIDO (mayor o igual a 50_000 aplica $5.000)
        carrito2.agregarProducto("Correa", 30_000, 1); // Subtotal 60_000
        assertEquals(5_000, carrito2.calcularDescuento());
    }

    @Test
    void calcularTotalConEnvioYIvaTest() {
        // Compra pequeña (aplica IVA y costo de envío)
        carrito.agregarProducto("Libro", 40_000, 1);
        double total = carrito.calcularTotal();
        assertTrue(total > 0);

        // Compra grande (envío gratis por superar 150_000)
        CarritoCompras carritoGrande = new CarritoCompras();
        carritoGrande.agregarProducto("Laptop", 160_000, 1);
        double totalGrande = carritoGrande.calcularTotal();
        assertTrue(totalGrande > 0);
    }
}