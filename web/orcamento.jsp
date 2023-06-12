<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>AutoBudget</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <script src="https://unpkg.com/vue@next/dist/vue.global.prod.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.2/pdfmake.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.2/vfs_fonts.js"></script>
    
    <style>
        body {
            background-color: #f8f9fa; /* Cor de fundo mais clara */
        }

        /* Personalize os estilos abaixo conforme necessário */
        label {
            color: #333; /* Cor do texto dos rótulos */
        }

        select, input[type="number"] {
            background-color: #fff; /* Cor de fundo dos campos de seleção e campos de entrada */
            color: #333; /* Cor do texto dos campos de seleção e campos de entrada */
            /* Outros estilos... */
        }

        .btn {
            background-color: #333; /* Cor de fundo dos botões */
            color: #fff; /* Cor do texto dos botões */
            /* Outros estilos... */
        }
    </style>
</head>
<body>
<%@include file="WEB-INF/jspf/header.jspf" %>
<div id="app" class="container">
    <h1>Add Peças</h1>

    <div class="input-group mb-3">
        <label for="modeloCarroSelect" class="input-group-text">Modelo Carro:</label>
        <select id="modeloCarroSelect" class="form-select" v-model="selectedModeloCarro" @change="updatePecas">
            <option value="" selected disabled>Select a Modelo Carro</option>
            <option v-for="modeloCarro in modeloCarros" :value="modeloCarro.rowId">{{ modeloCarro.nomeModelo }}</option>
        </select>

        <label for="pecaSelect" class="input-group-text">Peça:</label>
        <select id="pecaSelect" class="form-select" v-model="selectedPeca">
            <option value="" selected disabled>Select a Peça</option>
            <option v-for="peca in filteredPecas" :value="peca.rowId">{{ peca.nomePeca }}</option>
        </select>

        <label for="quantityInput" class="input-group-text">Quantity:</label>
        <input id="quantityInput" type="number" class="form-control" v-model.number="quantity">

        <button class="btn btn-primary" @click="addPeca">Add Peça</button>
    </div>

    <div v-if="error" class="alert alert-danger mt-3">{{ error }}</div>

    <h2 class="mt-4">Selected Peças:</h2>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">Peça</th>
            <th scope="col">Quantity</th>
            <th scope="col">Price</th>
            <th scope="col">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(selectedPeca, index) in selectedPecas" :key="selectedPeca.rowId">
            <td>{{ selectedPeca.nomePeca }}</td>
            <td>{{ selectedPeca.quantity }}</td>
            <td>{{ selectedPeca.preco * selectedPeca.quantity }}</td>
            <td><button class="btn btn-danger btn-sm" @click="removePeca(index)">Remove</button></td>
        </tr>
        </tbody>
    </table>

    <h3>Total Price: {{ calculateTotalPrice() }}</h3>

    <button class="btn btn-primary" @click="generatePDF">Generate PDF</button>
</div>

<script>
    const app = Vue.createApp({
        data() {
            return {
                selectedModeloCarro: null,
                selectedPeca: null,
                quantity: 0,
                error: "",
                modeloCarros: [],
                pecas: [],
                selectedPecas: []
            };
        },
        computed: {
            filteredPecas() {
                if (this.selectedModeloCarro) {
                    const modeloCarro = this.modeloCarros.find(mc => mc.rowId === this.selectedModeloCarro);
                    if (modeloCarro) {
                        return this.pecas.filter(peca => peca.modeloCarroRowId === modeloCarro.rowId);
                    }
                }
                return [];
            }
        },
        methods: {
            async fetchData() {
                try {
                    const responseModeloCarros = await fetch("/AutoBudget-2/api/modeloCarro");
                    const dataModeloCarros = await responseModeloCarros.json();
                    this.modeloCarros = dataModeloCarros.list;

                    const responsePecas = await fetch("/AutoBudget-2/api/peca");
                    const dataPecas = await responsePecas.json();
                    this.pecas = dataPecas.list;
                } catch (error) {
                    this.error = "Failed to fetch data.";
                }
            },
            updatePecas() {
                this.selectedPeca = null;
                this.quantity = 0;
            },
            addPeca() {
                if (this.selectedPeca && this.quantity > 0) {
                    const selectedPeca = this.pecas.find(peca => peca.rowId === this.selectedPeca);
                    if (selectedPeca) {
                        const newSelectedPeca = {
                            rowId: selectedPeca.rowId,
                            nomePeca: selectedPeca.nomePeca,
                            preco: selectedPeca.preco,
                            quantity: this.quantity
                        };
                        this.selectedPecas.push(newSelectedPeca);
                        this.selectedPeca = null;
                        this.quantity = 0;
                        this.error = "";
                    } else {
                        this.error = "Invalid Peça selected.";
                    }
                } else {
                    this.error = "Please select a Peça and enter a valid quantity.";
                }
            },
            removePeca(index) {
                this.selectedPecas.splice(index, 1);
            },
            calculateTotalPrice() {
                return this.selectedPecas.reduce((total, selectedPeca) => {
                    return total + selectedPeca.preco * selectedPeca.quantity;
                }, 0).toFixed(2);
            },
            generatePDF() {
                const docDefinition = {
                    content: [
                        {
                            text: 'Selected Peças Report',
                            style: 'header'
                        },
                        {
                            ul: this.selectedPecas.map(selectedPeca => [
                                `Peça: ${selectedPeca.nomePeca}`,
                                `Modelo Carro: ${this.getSelectedModeloCarro(selectedPeca)}`,
                                `Quantity: ${selectedPeca.quantity}`,
                                `Individual Value: ${selectedPeca.preco}`,
                                `Total Value: ${selectedPeca.preco * selectedPeca.quantity}`,
                                ' '
                            ])
                        },
                        {
                            text: `Total Price: ${this.calculateTotalPrice()}`,
                            style: 'total'
                        }
                    ],
                    styles: {
                        header: {
                            fontSize: 18,
                            bold: true,
                            marginBottom: 20
                        },
                        total: {
                            fontSize: 16,
                            bold: true,
                            marginTop: 20
                        }
                    }
                };

                pdfMake.createPdf(docDefinition).open();
            },
            getSelectedModeloCarro(selectedPeca) {
                const modeloCarro = this.modeloCarros.find(mc => mc.rowId === selectedPeca.modeloCarroRowId);
                return modeloCarro ? modeloCarro.nomeModelo : "";
            }
        },
        mounted() {
            this.fetchData();
        }
    });

    app.mount("#app");
</script>
</body>
</html>
