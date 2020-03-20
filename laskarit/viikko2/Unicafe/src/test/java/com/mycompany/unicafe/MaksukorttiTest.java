package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(1000);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void kortinSaldoAlussaOikein() {
        assertEquals("saldo: 10.0", kortti.toString());
    }
    
    @Test
    public void kortilleVoiLadataRahaa() {
        kortti.lataaRahaa(1000);
        assertEquals("saldo: 20.0", kortti.toString());
        
        kortti.lataaRahaa(200);
        assertEquals("saldo: 22.0", kortti.toString());
    }
    
    @Test
    public void otaRahaaKunSaldoRiittaa() {
        assertTrue(kortti.otaRahaa(200));
        assertEquals("saldo: 8.0", kortti.toString());
        
        
        assertTrue(kortti.otaRahaa(550));
        assertEquals("saldo: 2.50", kortti.toString());
    }
    
    @Test
    public void otaRahaaKunSaldoEiRiita() {
        assertFalse(kortti.otaRahaa(2000));
        assertEquals("saldo: 10.0", kortti.toString());
    }
    
    @Test
    public void saldoPalauttaaOikeanSaldon() {
        assertEquals(1000, kortti.saldo());
    }
}
