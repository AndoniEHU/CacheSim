function transformNumber() {
    const number = parseInt(document.getElementById('numberInput').value, 10);
    if (isNaN(number)) {
        alert('Por favor, introduce un número válido.');
        return;
    }

    let url = "http://localhost:8080/async/transform/"+number;
    fetch(url, { method: 'GET' })
        .then(Result => Result.json())
        .then(string => {

            // Printing our response
            console.log(string);

            // Printing our field of our response
            console.log(`Title of our response :  ${string.title}`);
        })
        .catch(errorMsg => { console.log(errorMsg); });
}

// Añadir un listener para el evento de carga de la página
window.onload = function() {
    document.getElementById('transformButton').addEventListener('click', transformNumber);
    console.log(config.tamBloque);
};
