package com.ac.cachesim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/async")
public class AsyncController {

    @Autowired
    mainController mainController;

    @GetMapping("/transform/{num}")
    public CacheBlock transformarNumero(@PathVariable("num") int num) {
        ConfigCache configCache = mainController.getConfigCache();
        if(configCache.getTamanioConjunto() == 1){
            return directTransformation(num,configCache);
        }else if(configCache.getTamanioConjunto() == 2 || configCache.getTamanioConjunto() == 4){
            return setTransformation(num,configCache);
        }else{
            return asociativeTransformation(num,configCache);
        }

    }

    @PostMapping("transform/{num}/{op}")
    public SimData operation(@PathVariable("num") int num, @PathVariable("op") String op, @RequestBody List<TableRow> tableBody) {
        ConfigCache configCache = mainController.getConfigCache();
        CacheBlock cacheBlock;

        if(configCache.getTamanioConjunto() == 1){

            cacheBlock = directTransformation(num,configCache);
            return directTableProcess(tableBody,op,cacheBlock);

        }else if(configCache.getTamanioConjunto() == 2 || configCache.getTamanioConjunto() == 4){

            cacheBlock = setTransformation(num,configCache);
            if(configCache.getTamanioConjunto() == 2){
                return twoSetTableProcess(tableBody,op,cacheBlock,configCache.getPoliticaReemplazo());
            }else{
                return fourSetTableProcess(tableBody,op,cacheBlock,configCache.getPoliticaReemplazo());
            }

        }else{

            cacheBlock = asociativeTransformation(num,configCache);
            return asociativeTableProcess(tableBody,op,cacheBlock,configCache.getPoliticaReemplazo());

        }
    }

    private SimData fourSetTableProcess(List<TableRow> tableBody, String op, CacheBlock cacheBlock, String politica) {

        int hitIndex = -1;
        boolean hit = false;
        int tiempoOp = 0;
        int emptyIndex = -1;
        int maxTime = 0;
        int maxTimeIndex = 0;
        int firstInd;
        if(cacheBlock.getSet() == 0){
            firstInd = 0;
        }else{
            firstInd=4;
        }

        for(int i = firstInd;i<firstInd+4;i++){
            TableRow tableRow = tableBody.get(i);
            if(maxTime < tableRow.getTiempo() && hitIndex == -1){
                maxTime = tableRow.getTiempo();
                maxTimeIndex = i;
            }
            if(tableRow.getBloqueMP() == -1  && hitIndex == -1){
                emptyIndex = i;
                break;
            }
            if(tableRow.getEtiqueta() == cacheBlock.getTag() && hitIndex == -1){
                hitIndex = i;
                maxTime = tableRow.getTiempo();
            }
        }
        sumTimesFourSets(tableBody, hitIndex, emptyIndex, maxTimeIndex, maxTime, politica, cacheBlock, op);
        if(hitIndex == -1){
            if(emptyIndex != -1){
                tiempoOp = 30;
            }else{
                tiempoOp = 58;
            }
        }else{
            hit = true;
            tiempoOp = 3;
        }
        return new SimData(tableBody,hit,tiempoOp);
    }

    private void sumTimesFourSets(List<TableRow> tableBody, int hitIndex, int emptyIndex, int maxTimeIndex, int maxTime, String politica, CacheBlock cacheBlock, String op) {
        if(emptyIndex != -1){
            tableBody.get(emptyIndex).setValido(1);
            tableBody.get(emptyIndex).setTiempo(1);
            tableBody.get(emptyIndex).setBloqueMP(cacheBlock.getBlock());
            tableBody.get(emptyIndex).setEtiqueta(cacheBlock.getTag());
            tableBody.get(emptyIndex).setModificado(op.equals("st") ? 1 : 0);
            if(emptyIndex < 4){
                for(int i=0;i<emptyIndex;i++){
                    tableBody.get(i).setTiempo(tableBody.get(i).getTiempo() + 1);
                }
            }else{
                for(int i=4;i<emptyIndex;i++){
                        tableBody.get(i).setTiempo(tableBody.get(i).getTiempo() + 1);
                }
            }
        } else if(hitIndex != -1){
            tableBody.get(hitIndex).setModificado(op.equals("st") ? 1 : tableBody.get(hitIndex).getModificado());
            if(politica.equals("LRU")){
                tableBody.get(hitIndex).setTiempo(1);
                if(hitIndex < 4){
                    for(int i=0;i<4;i++){
                        if (tableBody.get(i).getTiempo() < maxTime && i != hitIndex){
                            tableBody.get(i).setTiempo(tableBody.get(i).getTiempo() + 1);
                        }
                    }
                }else{
                    for(int i=4;i<8;i++){
                        if (tableBody.get(i).getTiempo() < maxTime && i != hitIndex){
                            tableBody.get(i).setTiempo(tableBody.get(i).getTiempo() + 1);
                        }
                    }
                }
            }
        }else{
            tableBody.get(maxTimeIndex).setValido(1);
            tableBody.get(maxTimeIndex).setTiempo(1);
            tableBody.get(maxTimeIndex).setBloqueMP(cacheBlock.getBlock());
            tableBody.get(maxTimeIndex).setEtiqueta(cacheBlock.getTag());
            tableBody.get(maxTimeIndex).setModificado(op.equals("st") ? 1 : 0);
            if(maxTimeIndex < 4){
                for(int i=0;i<4;i++){
                    if (tableBody.get(i).getTiempo() < maxTime && i != maxTimeIndex){
                        tableBody.get(i).setTiempo(tableBody.get(i).getTiempo() + 1);
                    }
                }
            }else{
                for(int i=4;i<8;i++){
                    if (tableBody.get(i).getTiempo() < maxTime && i != maxTimeIndex){
                        tableBody.get(i).setTiempo(tableBody.get(i).getTiempo() + 1);
                    }
                }
            }
        }
    }

    private SimData twoSetTableProcess(List<TableRow> tableBody, String op, CacheBlock cacheBlock, String politica) {
        int hitIndex = -1;
        boolean hit = false;
        int tiempoOp = 0;
        int emptyIndex = -1;
        int maxTime = 0;
        int maxTimeIndex = 0;
        int firstInd = 0;
        if(cacheBlock.getSet() == 1) firstInd = 2;
        if(cacheBlock.getSet() == 2) firstInd = 4;
        if(cacheBlock.getSet() == 3) firstInd = 6;

        for(int i=firstInd;i<firstInd+2;i++){
            TableRow tableRow = tableBody.get(i);
            if(maxTime < tableRow.getTiempo() && hitIndex == -1){
                maxTime = tableRow.getTiempo();
                maxTimeIndex = i;
            }
            if(tableRow.getBloqueMP() == -1  && hitIndex == -1){
                emptyIndex = i;
                break;
            }
            if(tableRow.getEtiqueta() == cacheBlock.getTag() && hitIndex == -1){
                hitIndex = i;
                maxTime = tableRow.getTiempo();
            }
        }
        sumTimesTworSets(tableBody, hitIndex, emptyIndex, maxTimeIndex, maxTime, politica, cacheBlock, op);
        if(hitIndex == -1){
            if(emptyIndex != -1){
                tiempoOp = 30;
            }else{
                tiempoOp = 58;
            }
        }else{
            hit = true;
            tiempoOp = 3;
        }
        return new SimData(tableBody,hit,tiempoOp);
    }

    private void sumTimesTworSets(List<TableRow> tableBody, int hitIndex, int emptyIndex, int maxTimeIndex, int maxTime, String politica, CacheBlock cacheBlock, String op) {
        if(emptyIndex != -1){
            tableBody.get(emptyIndex).setValido(1);
            tableBody.get(emptyIndex).setTiempo(1);
            tableBody.get(emptyIndex).setBloqueMP(cacheBlock.getBlock());
            tableBody.get(emptyIndex).setEtiqueta(cacheBlock.getTag());
            tableBody.get(emptyIndex).setModificado(op.equals("st") ? 1 : 0);
            if(emptyIndex % 2 == 0){
                tableBody.get(emptyIndex+1).setTiempo(2);
            }else{
                tableBody.get(emptyIndex-1).setTiempo(2);
            }

        } else if(hitIndex != -1){
            tableBody.get(hitIndex).setModificado(op.equals("st") ? 1 : tableBody.get(hitIndex).getModificado());
            if(politica.equals("LRU")){
                tableBody.get(hitIndex).setTiempo(1);
                if(hitIndex % 2 == 0){
                    tableBody.get(hitIndex+1).setTiempo(2);
                }else{
                    tableBody.get(hitIndex-1).setTiempo(2);
                }
            }
        }else{
            tableBody.get(maxTimeIndex).setValido(1);
            tableBody.get(maxTimeIndex).setTiempo(1);
            tableBody.get(maxTimeIndex).setBloqueMP(cacheBlock.getBlock());
            tableBody.get(maxTimeIndex).setEtiqueta(cacheBlock.getTag());
            tableBody.get(maxTimeIndex).setModificado(op.equals("st") ? 1 : 0);
            if(maxTimeIndex % 2 == 0){
                tableBody.get(maxTimeIndex+1).setTiempo(2);
            }else{
                tableBody.get(maxTimeIndex-1).setTiempo(2);
            }
        }

    }

    private SimData asociativeTableProcess(List<TableRow> tableBody, String op, CacheBlock cacheBlock,String politica) {

        int hitIndex = -1;
        boolean hit = false;
        int tiempoOp = 0;
        int emptyIndex = -1;
        int maxTime = 0;
        int maxTimeIndex = 0;

        for(int i=0;i<8;i++){
            TableRow tableRow = tableBody.get(i);
            if(maxTime < tableRow.getTiempo() && hitIndex == -1){
                maxTime = tableRow.getTiempo();
                maxTimeIndex = i;
            }
            if(tableRow.getBloqueMP() == -1  && hitIndex == -1){
                emptyIndex = i;
                break;
            }
            if(tableRow.getEtiqueta() == cacheBlock.getTag() && hitIndex == -1){
                hitIndex = i;
                maxTime = tableRow.getTiempo();
            }
        }

        sumTimesAsociative(tableBody, hitIndex, emptyIndex, maxTimeIndex, maxTime, politica, cacheBlock, op);

        if(hitIndex == -1){
            if(emptyIndex != -1){
                tiempoOp = 30;
            }else{
                tiempoOp = 58;
            }
        }else{
            hit = true;
            tiempoOp = 3;
        }
        return new SimData(tableBody,hit,tiempoOp);

    }

    private void sumTimesAsociative(List<TableRow> tableBody, int hitIndex, int emptyIndex, int maxTimeIndex, int maxTime, String politica, CacheBlock cacheBlock, String op) {
            if(emptyIndex != -1){
                tableBody.get(emptyIndex).setValido(1);
                tableBody.get(emptyIndex).setTiempo(1);
                tableBody.get(emptyIndex).setBloqueMP(cacheBlock.getBlock());
                tableBody.get(emptyIndex).setEtiqueta(cacheBlock.getTag());
                tableBody.get(emptyIndex).setModificado(op.equals("st") ? 1 : 0);
                for(int i=0;i<emptyIndex;i++){
                    tableBody.get(i).setTiempo(tableBody.get(i).getTiempo() + 1);
                }
            } else if(hitIndex != -1){
                tableBody.get(hitIndex).setModificado(op.equals("st") ? 1 : tableBody.get(hitIndex).getModificado());
                if(politica.equals("LRU")){
                    tableBody.get(hitIndex).setTiempo(1);
                    for(int i=0;i<8;i++){
                        if (tableBody.get(i).getTiempo() < maxTime && i != hitIndex){
                            tableBody.get(i).setTiempo(tableBody.get(i).getTiempo() + 1);
                        }
                    }
                }
            }else{
                tableBody.get(maxTimeIndex).setValido(1);
                tableBody.get(maxTimeIndex).setTiempo(1);
                tableBody.get(maxTimeIndex).setBloqueMP(cacheBlock.getBlock());
                tableBody.get(maxTimeIndex).setEtiqueta(cacheBlock.getTag());
                tableBody.get(maxTimeIndex).setModificado(op.equals("st") ? 1 : 0);
                for(int i=0;i<8;i++){
                    if (tableBody.get(i).getTiempo() < maxTime && i != maxTimeIndex){
                        tableBody.get(i).setTiempo(tableBody.get(i).getTiempo() + 1);
                    }
                }
            }


    }


    private SimData directTableProcess(List<TableRow> tableBody, String op, CacheBlock cacheBlock) {

            boolean hit = false;
            int opTime = 0;
            TableRow tableRow = tableBody.get(cacheBlock.getSet());
            tableRow.setValido(1);
            tableRow.setTiempo(1);
            if(tableRow.getEtiqueta() == cacheBlock.getTag()){
                hit = true;
                opTime = 3;
                if(op.equals("st")){
                    tableRow.setModificado(1);
                    tableRow.setEtiqueta(cacheBlock.getTag());
                    tableRow.setBloqueMP(cacheBlock.getBlock());
                }else{
                    tableRow.setModificado(tableRow.getModificado() == 0 ? 0 : tableRow.getModificado());
                }
            }else{
                tableRow.setEtiqueta(cacheBlock.getTag());
                tableRow.setBloqueMP(cacheBlock.getBlock());
                opTime = 30;
                if(op.equals("st")){
                    tableRow.setModificado(1);
                }else{
                    tableRow.setModificado(0);
                }
            }
            tableBody.set(cacheBlock.getSet(),tableRow);
            return new SimData(tableBody,hit,opTime);

    }


    private CacheBlock asociativeTransformation(int direccion,ConfigCache configCache) {
        int bloque = direccion / configCache.getTamanioBloque();
        int tag = bloque;
        int set = (int) (Math.random() * 8);
        return new CacheBlock(bloque,tag,set);
    }

    private CacheBlock setTransformation(int direccion,ConfigCache configCache) {
        int bloque = direccion / configCache.getTamanioBloque();
        int tag = bloque / (8/configCache.getTamanioConjunto());
        int set = bloque % (8/configCache.getTamanioConjunto());
        return new CacheBlock(bloque,tag,set);
    }

    private CacheBlock directTransformation(int direccion,ConfigCache configCache) {
        int bloque = direccion / configCache.getTamanioBloque();
        int tag = bloque / (8);
        int set = bloque % (8);

        return new CacheBlock(bloque,tag,set);
    }

}
