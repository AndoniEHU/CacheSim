package com.ac.cachesim;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class mainController {

    private ConfigCache configCache = new ConfigCache();


    @GetMapping("/")
    public String config(Model model) {
        model.addAttribute("configCache", configCache);
        return "config";
    }

    @PostMapping("/config")
    public String handleConfig(ConfigCache configCache) {
        this.configCache.setTamanioBloque(configCache.getTamanioBloque());
        this.configCache.setTamanioPalabra(configCache.getTamanioPalabra());
        this.configCache.setTamanioConjunto(configCache.getTamanioConjunto());
        this.configCache.setPoliticaReemplazo(configCache.getPoliticaReemplazo());
        return "redirect:/cacheSim";
    }


    @GetMapping("/cacheSim")
    public String home(Model model) {

        model.addAttribute("tamBloque", configCache.getTamanioBloque());
        model.addAttribute("tamPalabra", configCache.getTamanioPalabra());
        model.addAttribute("tamConjunto", configCache.getTamanioConjunto());
        model.addAttribute("algReemplazo", configCache.getPoliticaReemplazo());

        return "cacheSim";
    }

    public ConfigCache getConfigCache() {
        return configCache;
    }

    public void setConfigCache(ConfigCache configCache) {
        this.configCache = configCache;
    }
}
