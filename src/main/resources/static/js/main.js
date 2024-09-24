
     async function transformData(number,operation) {

        let tb = document.querySelector(".cache-body");
        let filas = tb.getElementsByTagName("tr");
        let tableData = [];
        for (let fila of filas){
            let celdas = fila.getElementsByTagName("td");

            let data =  {
                conjunto : (celdas[0].innerText !== "") ? parseInt((celdas[0]).innerText) : 0,
                valido :  (celdas[1].innerText !== "") ? parseInt((celdas[1]).innerText) : 0,
                modificado : (celdas[2].innerText !== "") ? parseInt((celdas[2]).innerText) : -1,
                etiqueta : (celdas[3].innerText !== "") ? parseInt((celdas[3]).innerText) : -1,
                tiempo : (celdas[4].innerText !== "") ? parseInt((celdas[4]).innerText) : -1,
                bloqueMP : (celdas[5].innerText !== "") ? parseInt((celdas[5]).innerText) : -1,

            };
            tableData.push(data);
        }
        let url = "http://localhost:8080/async/transform/"+number+"/"+operation;
        fetch(url,
            {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body : JSON.stringify(tableData)
            }
            )
            .then(Result => Result.json())
            .then(data => {
                tB = data.tableBody;
                hit = data.hit;
                tOp = data.opTime;
                showData(data.tableBody,data.hit,data.opTime);
                stadistics(data.hit,data.opTime);


            })
            .catch(errorMsg => {
                console.log(errorMsg);
            });

    }

    function stadistics(h,t){
        let hitOMiss = "MISS";
        let tUltimaOp=0;
        let nOperaciones = document.querySelector(".n-operaciones").innerHTML;
        let tOperaciones = document.querySelector(".t-operaciones").innerHTML;
        let totalHits = document.querySelector(".total-hits").innerHTML;
        let tasaHits=0;
        console.log(document.querySelector(".n-operaciones").innerHTML)
        if(h === true){
            hitOMiss = "HIT";
            totalHits = parseInt(totalHits) + 1;
            document.querySelector(".h-m").innerHTML = hitOMiss;
            document.querySelector(".total-hits").innerHTML = totalHits;
        }else{
            document.querySelector(".h-m").innerHTML = hitOMiss;
        }
        tUltimaOp = t;
        nOperaciones= parseInt(nOperaciones) + 1;
        tOperaciones = parseInt(tOperaciones) + tUltimaOp;
        if(parseInt(totalHits) !== 0) {
            tasaHits = (parseInt(totalHits) / parseInt(nOperaciones));
        }
        document.querySelector(".n-operaciones").innerHTML = nOperaciones;
        document.querySelector(".t-ultimaOp").innerHTML = tUltimaOp;
        document.querySelector(".t-operaciones").innerHTML = tOperaciones;
        document.querySelector(".tasa-hits").innerHTML = (parseInt(totalHits) / parseInt(nOperaciones)).toFixed(3);

    }

     async function transformNumber() {

        const number = parseInt(document.getElementById('numberInput').value, 10);
        const operation = document.querySelector('input[name="format"]:checked').value;
        if (isNaN(number)) {
            alert('Por favor, introduce un número válido.');
            return;
        }

       await transformData(number,operation);




    }

    async function showData(tb,hit,Top){
        let cacheBody = document.querySelector(".cache-body")
        let rows = cacheBody.getElementsByTagName("tr");
        let i=0;
        for(let row of rows){
            if(tb[i].bloqueMP !== -1) {
                row.children[1].innerHTML = tb[i].valido;
                row.children[2].innerHTML = tb[i].modificado;
                row.children[3].innerHTML = tb[i].etiqueta;
                row.children[4].innerHTML = tb[i].tiempo;
                row.children[5].innerHTML = tb[i].bloqueMP;
            }
            i++;
        }
    }

// Añadir un listener para el evento de carga de la página
document.addEventListener('DOMContentLoaded', () => {
    {
        let tb = document.querySelector(".cache-body");
        let filas = tb.getElementsByTagName("tr");
        let i = 0;
        let j = 0;
        let tamC = config.tamConjunto;
        for (let fila of filas) {
            if(tamC === 2){
                fila.children[0].innerHTML = i % tamC;
                fila.setAttribute("class", "set" + j);
                if(1 === i % tamC){
                    j+=1;
                }
            }else if (tamC === 4) {

                fila.children[0].innerHTML = i % tamC;
                fila.setAttribute("class", "set" + j);
                if(3 === i % tamC){
                    j+=1;
                }

            } else if (tamC === 1) {
                fila.children[0].innerHTML = i;
                fila.setAttribute("class", "set" + i );
            }
            i++;
        }
        document.getElementById('transformButton').addEventListener('click', transformNumber);
    }
});
