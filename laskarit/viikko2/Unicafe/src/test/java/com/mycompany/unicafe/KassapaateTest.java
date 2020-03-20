/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author okkokuisma
 */
public class KassapaateTest {
    Kassapaate kassa;
    Maksukortti kortti;
    
    public KassapaateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(500);
    }
    
    @Test
    public void uudessaKassapaatteessaOikeaSaldo() {
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void uudessaKassapaatteessaOikeaLounaitaMyyty() {
        assertEquals(0, kassa.maukkaitaLounaitaMyyty() + kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void syiEdullisestiKateisellaKunMaksuRiittava() {
        //syoEdullisesti
        assertEquals(kassa.syoEdullisesti(500), 260);
        assertEquals(100240, kassa.kassassaRahaa());
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void syoMaukkaastiKateisellaKunMaksuRiittava() {
        //syoMaukkaasti
        assertEquals(kassa.syoMaukkaasti(500), 100);
        assertEquals(100400, kassa.kassassaRahaa());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kateismaksuKunMaksuEiRiittava() {
        //syoEdullisesti
        assertEquals(kassa.syoEdullisesti(200), 200);
        
        //syoMaukkaasti
        assertEquals(kassa.syoMaukkaasti(300), 300);
        
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.edullisiaLounaitaMyyty() + kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoEdullisestiKunKortillaSaldoa() {
        assertTrue(kassa.syoEdullisesti(kortti));
        assertEquals(260, kortti.saldo());
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void syoMaukkaastiKunKortillaSaldoa() {
        assertTrue(kassa.syoMaukkaasti(kortti));
        assertEquals(100, kortti.saldo());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoEdullisestiKortillaKunEiSaldoa() {
        kortti.otaRahaa(500);
        assertFalse(kassa.syoEdullisesti(kortti));
        assertEquals(0, kortti.saldo());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void syoMaukkaastiKortillaKunEiSaldoa() {
        kortti.otaRahaa(500);
        assertFalse(kassa.syoMaukkaasti(kortti));
        assertEquals(0, kortti.saldo());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void lataaRahaaKortillaToimiiPositiivisellaSummalla() {
        kassa.lataaRahaaKortille(kortti, 250);
        assertEquals(750, kortti.saldo());
        assertEquals(100250, kassa.kassassaRahaa());
    }
    
    @Test
    public void lataaRahaaKortilleEiToimiNegatiivisellaSummalla() {
        kassa.lataaRahaaKortille(kortti, -500);
        assertEquals(500, kortti.saldo());
        assertEquals(100000, kassa.kassassaRahaa());
    }
}
