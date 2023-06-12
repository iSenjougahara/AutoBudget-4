<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Estoque</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #f8f9fa;
        }

        .container {
            display: flex;
            flex-direction: column;
            
            justify-content: center;
            min-height: 10vh;
            left: 0;
            right: 0;
        }


        .btn-success {
            background-color: black;
            border-color: black;
        }

        .btn-danger {
            background-color: black;
            border-color: black;
        }

        .modal-content {
            background-color: #fff;
        }

        .table {
            background-color: #f8f9fa;
        }
    </style>
    <script src="https://unpkg.com/vue@next"></script>
</head>
<body>
<%@include file="WEB-INF/jspf/header.jspf" %>
<div id="app" class="container">
    <div v-if="shared.session">
        <div v-if="error" class="alert alert-danger m-2" role="alert">
            {{error}}
        </div>
        <div v-else>
            <h2>Gerenciamento de Estoque</h2>
            <button type="button" @click="removeUser(item.rowId)" class="btn btn-success btn-sm" data-bs-toggle="modal" data-bs-target="#addUserModal">
                <i class="bi bi-plus"></i> Adicionar
            </button>

            <!-- Modal -->
            <div class="modal fade" id="addUserModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="exampleModalLabel">Adicionar Produto</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form>
                                <div class="mb-3">
                                    <label for="inputCdpeca" class="form-label">N° Peca</label>
                                    <input type="text" v-model="newcdpeca" class="form-control" id="inputCdpeca">
                                </div>
                                <div class="mb-3">
                                    <label for="inputFilial" class="form-label">Filial</label>
                                    <input type="text" v-model="newFilial" class="form-control" id="inputFilial">
                                </div>

                                <div class="mb-3">
                                    <label for="inputQuantidade" class="form-label">Quantidade</label>
                                    <input type="number" v-model="newQuantidade" class="form-control" id="inputQuantidade">
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="button" class="btn btn-primary" data-bs-dismiss="modal" @click="addEstoque()">Inserir</button>
                        </div>
                    </div>
                </div>
            </div>
            <table class="table">
                <tr>
                    <th>ID</th>
                    <th>COD PEÇA</th>
                    <th>NOME</th>
                    <th>FILIAL</th>
                    <th>QUANTIDADE</th>
                    <th>DATA MODIFICAÇÃO</th>
                </tr>
                <tr v-for="item in list" :key="item.rowId">
                    <td>{{ item.rowId }}</td>
                    <td>{{ item.peca }}</td>
                    <td>{{ item.nomepeca }}</td>
                    <td>{{ item.filial }}</td>
                    <td>{{ item.quantidade }}</td>
                    <td>{{ item.create_date }}</td>
                    <td>
                        <button type="button" @click="removeUser(item.rowId)" class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#finishModal">
                            <i class="bi bi-trash"></i> Remover
                        </button>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
<script>
    const app = Vue.createApp({
        data() {
            return {
                shared: shared,
                error: null,
                cdpeca: '',
                nomepeca: '',
                filial: '',
                quantidade: '',
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
            },
            async loadList() {
                const data = await this.request("/AutoBudget-2/api/estoques", "GET");
                if (data) {
                    this.list = data.list;
                }
            },
            async addEstoque() {
                const data = await this.request("/AutoBudget-2/api/estoques", "POST", {cdpeca: this.newcdpeca, filial: this.newFilial, quantidade: this.newQuantidade});
                if (data) {
                    this.newcdpeca = '';
                    this.newnomepeca = '';
                    this.newFilial = '';
                    this.newQuantidade = '';
                    await this.loadList();
                }
            },

            async removeUser(id) {
                const data = await this.request("/AutoBudget-2/api/estoques?id=" + id, "DELETE");
                if (data) {
                    await this.loadList();
                }
            }
        },
        mounted() {
            this.loadList();
        }
    });
    app.mount('#app');
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe"
        crossorigin="anonymous"></script>
</body>
</html>
