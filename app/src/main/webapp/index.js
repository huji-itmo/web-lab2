
const API_ENDPOINT = "/controller"

const handleResponseError = async (responseText) => {
    console.log(responseText);
    let bodyMessage = responseText.split("<body>", 2)[1].split("</body>", 2)[0];
    alert(bodyMessage);
}

class FormValidator {
    form_x_input;
    form_y_input;
    form_r_input;

    checkXInput = () => {
        if (this.form_x_input.value === "") {
            alert("Выберите значение.");
            return false;
        }

        // alert("");
        return true;
    };

    checkYInput = () => {
        if (this.form_y_input.value === "") {
            alert(
                "Это поле не должно быть пустым."
            );
            return false;
        }

        let pattern = new RegExp(/^-?\d+([\.,]\d+)?$/, "g");

        if (!pattern.test(this.form_y_input.value)) {
            alert("Введите число.");
            return false;
        }

        if (this.form_y_input.value > 3 || this.form_y_input.value < -3) {
            alert(
                "Число должно быть в пределе [-3, 3]."
            );
            return false;
        }

        // alert("");
        return true;
    };

    checkRInput = () => {
        if (this.form_r_input.value === "") {
            alert("Выберите значение.");
            return false;
        }

        if (!["1", "2", "3", "4", "5"].includes(this.form_r_input.value)) {
            this.form_r_input.value = "";
            alert("Выберите НОРМАЛЬНОЕ значение.");
            return false;
        }

        // alert("");
        return true;
    };

    constructor() {
        this.form_x_input = $("#form-x-input")[0];
        this.form_y_input = $("#form-y-input")[0];
        this.form_r_input = $("#form-r-input")[0];

        $(".form-r-buttons").each((i, element) => {
            $(element).on("click", () => {
                $(this.form_r_input).val($(element).val());

                $(".form-r-buttons").each((i, removeClass) => {
                    $(removeClass).removeClass("r-button-selected");
                });

                $(element).addClass("r-button-selected");

                changeBoardR($(this.form_r_input).val());
            });
        });

        // let checkRFunction = () => {this.checkRInput()}
        // let checkXFunction = () => {this.checkXInput()}
        // let checkYFunction = () => {this.checkYInput()}

        // this.form_r_input.oninvalid = checkRFunction
        // this.form_r_input.oninput = checkRFunction
        
        // this.form_x_input.oninput = checkXFunction
        // this.form_x_input.oninvalid = checkXFunction

        // this.form_y_input.oninvalid = checkYFunction
        // this.form_y_input.oninput = checkYFunction

        // $("#submit-button").on("click", onSubmit)

        // document.forms.main.onsubmit = onSubmit;    
    }

    checkAllValid = () => {
        let isNotValid = [
            !this.checkRInput(),
            !this.checkYInput(),
            !this.checkXInput(),
        ].reduce((prev, curr) => prev || curr);
    
        return !isNotValid;
    }
}

const form_validator = new FormValidator();
const table_element = $("#table");

var onSubmit = (event) => {

    if (!form_validator.checkAllValid()) {
        // alert("дерьмо");
        
        return;
    }

    sendRequest(form_validator.form_x_input.value, form_validator.form_y_input.value, form_validator.form_r_input.value);
} 

document.getElementById("submit-button").addEventListener("click", onSubmit);


var sendRequest = async (x,y,r) => {
    let request_body = JSON.stringify({ x: x, y: y, r: r });

    $.ajax({
        url: "/controller",
        data: request_body,
        type: "POST",
        headers: {
            accept: "application/json",
            "Content-Type": "application/json"
        },
        dataType: "json",
        success: function (data) {
            if (data["error"] != undefined) {
                alert(data.error);
                return;
            }

            addPointFromData(data);
        },
        error: function (xhr, status, error) {
            
            handleResponseError(xhr.responseText);
        }
    });
}

const addPointFromData = (json) => {
    let tableEntry = "<tr>";
    let data = [json.x, json.y, json.r, json.hit, json.duration_milliseconds, json.server_time];
    data.forEach((str) => {
        tableEntry += `<td>${str}</td>`
    });
    tableEntry += "</tr>";

    table_element.html(table_element.html() + tableEntry);

    createNewHitPoint(
        json.x,
        json.y,
        json.r,
        {
            size: pointRadius,
            name: json.server_time,
            color: json.hit ? "green" : "red",
        }
    )
}


async function loadHitHistory() {

    changeBoardR(1);

    let response = await fetch(API_ENDPOINT, {
        method: "GET",
        headers: {
            accept: "application/json",
        },
    }).catch(errorResponse => {
        handleResponseError(errorResponse);
    });

    if (!response.ok) {
        return;
    }

    let json = await response.json();

    if (json["error"] != undefined) {
        alert(json.error);
        return;
    }
    
    let i = 0;
    while(json[i] != undefined) {
        createNewHitPoint(
            json[i].x,
            json[i].y,
            json[i].r,
            {
                size: pointRadius,
                name: json[i].server_time,
                color: json[i].hit ? "green" : "red",
            }
        )
        i++;
    }
}

loadHitHistory();
