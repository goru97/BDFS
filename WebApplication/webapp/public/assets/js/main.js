type = ['','info','success','warning','danger'];


showSuccessNotification = function(from, align, message){
        $.notify({
            icon: "pe-7s-check",
            message: message

        },{
            type: type[2],
            timer: 2000,
            placement: {
                from: from,
                align: align
            }
        })}

showWarningNotification= function(from, align, message){
    $.notify({
        icon: "pe-7s-attention",
        message: "<b>Warning - </b>" + message

    },{
        type: type[3],
        timer: 2000,
        placement: {
            from: from,
            align: align
        }
    })}

showFailureNotification= function(from, align){
    $.notify({
        icon: "pe-7s-close-circle",
        message: "<b>Error - </b>Threshold field can not be blank"

    },{
        type: type[4],
        timer: 2000,
        placement: {
            from: from,
            align: align
        }
    })}

showInfoNotification = function(from, align, message){
    $.notify({
        icon: "pe-7s-check",
        message: message

    },{
        type: type[1],
        timer: 2000,
        placement: {
            from: from,
            align: align
        }
    })}

document.getElementById("login_button").addEventListener("click", function () {
    location.href = "user.html";
});
