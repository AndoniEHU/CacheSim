package com.ac.cachesim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.Integer.toBinaryString;

@RestController
@RequestMapping("/async")
public class AsyncController {

    @Autowired
    mainController mainController;

    @GetMapping("/transform/{num}")
    public int[] transformarNumero(@PathVariable("num") int num) {
        ConfigCache configCache = mainController.getConfigCache();
        if(configCache.getTamanioConjunto() == 1){
            return directTransformation(num,configCache);
        }else if(configCache.getTamanioConjunto() == 2 || configCache.getTamanioConjunto() == 4){
            return setTransformation(num,configCache);
        }else{
            return asociativeTransformation(num,configCache);
        }

    }

    private int[] asociativeTransformation(int num,ConfigCache configCache) {
        return null;
    }

    private int[] setTransformation(int num,ConfigCache configCache) {
        return null;
    }

    private int[] directTransformation(int num,ConfigCache configCache) {
        int bitsByte = (int) (Math.log(num)/Math.log(2));
        int bitsPalabra = (int)((Math.log((double) configCache.getTamanioBloque() /configCache.getTamanioPalabra()))/Math.log(2));
        int dirPalAbs = (int)(num / Math.pow(2,bitsByte));
        int b = (int)(num % Math.pow(2,bitsByte));
        int bloqueMP = (int)(dirPalAbs / Math.pow(2,bitsPalabra));
        int palabra = (int)(dirPalAbs % Math.pow(2,bitsPalabra));
        int tag = bloqueMP/ configCache.getTamanioConjunto();
        int bloque = bloqueMP % configCache.getTamanioConjunto();

        return new int[]{tag,bloque,palabra,b};
    }

}
