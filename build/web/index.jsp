<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>AutoBudget</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <script src="https://unpkg.com/vue@next"></script>
    <style>
        body {
            background-color:darkgrey ;
        }
        .container {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .form-container {
            width: 300px;
        }
        .form-control {
            background-color: yellow;
            color: black;
        }
        .btn-primary {
            background-color: blue;
            border-color: blue;
        }
    </style>
</head>
<body>
<%@include file="WEB-INF/jspf/header.jspf" %>
<div id="app" class="container">
    <div v-if="shared.session">
        <div v-if="error" class="alert alert-danger" role="alert">
            {{error}}
        </div>
        <div v-else>
            <a href="https://cakeerp.com/"><img src="https://cakeerp.com/wp-content/uploads/2018/12/gest%C3%A3o-de-estoque-768x427.png" alt="some text" width="500" height="300" title="texto" /></a>
        </div>
    </div>
    <div class="form-container">
        <!-- Seu formulário aqui -->
    </div>
</div>

<script>
    const app = Vue.createApp({
        data() {
            return {
                shared: shared,
                error: null,
                now: new Date(),
                newModel: '',
                newColor: '',
                newPlate: '',
                hourPrice: 0.0,
                list: [],
            }
        },
        methods: {
            async request(url = "", method, data) {
                try {
                    const response = await fetch(url, {
                        method: method,
                        headers: {"Content-Type": "application/json"},
                        body: JSON.stringify(data)
                    });
                    if (response.status == 200) {
                        return response.json();
                    } else {
                        this.error = response.statusText;
                    }
                } catch (e) {
                    this.error = e;
                    return null;
                }
            }
            //async loadList() {
            //    const data = await this.request("/AutoBudget/api/parking", "GET");
            //    if(data) {
            //        this.hourPrice = data.hourPrice;
            //        this.list = data.list;
            //    }
            //}

        },
        mounted() {
            this.loadList();
            setInterval(() => {
                this.now = new Date();
            }, 1000);
        }
    });
    app.mount('#app');
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4dd
