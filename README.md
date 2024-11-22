
# Aplicativo de Calculadora de Painéis Solares

## Equipe de Desenvolvimento
- **Razyel Ferrari** (RM551875) - GitHub: [irazyel](https://github.com/irazyel)
- **Rayzor Anael** (RM551832) - GitHub: [rozyar](https://github.com/rozyar)
- **Derick Araújo** (RM551007) - GitHub: [dericki](https://github.com/dericki)
- **Kalel Schlichting** (RM550620) - GitHub: [K413l](https://github.com/K413l)

---

## Introdução

Este projeto consiste em um aplicativo Android desenvolvido em **Kotlin** que auxilia os usuários a calcular a quantidade de painéis solares necessários para suprir o consumo energético de seus eletrodomésticos. O aplicativo permite que os usuários:

- Cadastrem seus eletrodomésticos.
- Realizem análises com base nas horas de luz solar disponíveis.
- Visualizem o histórico dessas análises.

O backend do projeto foi desenvolvido em **.NET** com autenticação **JWT**, disponível no repositório:

- [Backend em .NET](https://github.com/rozyar/Dotnet-Global-Solution)

---

## Requisitos Atendidos

1. **Tela Principal com Menu ou Navegação (10 Pontos)**
   - O aplicativo possui uma tela principal (`HomeFragment`) com um menu de navegação inferior (`BottomNavigationView`) que permite acessar diferentes funcionalidades.

2. **Tela para Inserção de Dados (10 Pontos)**
   - Existe uma tela para inserção de eletrodomésticos (`AppliancesFragment`), onde o usuário pode adicionar novos itens.

3. **Tela para Listagem dos Dados (10 Pontos)**
   - Os eletrodomésticos cadastrados são listados na mesma tela de inserção (`AppliancesFragment`), permitindo que o usuário visualize todos os itens.

4. **Tela para Edição da Informação (10 Pontos)**
   - O usuário pode editar informações dos eletrodomésticos já cadastrados, através de um diálogo de edição que aparece ao clicar no botão de editar em cada item da lista.

5. **Opção de Exclusão dos Dados (10 Pontos)**
   - Há a opção de excluir eletrodomésticos cadastrados, através do botão de exclusão presente em cada item da lista.

6. **Uso de API Desenvolvida no Backend em .NET (10 Pontos)**
   - O aplicativo consome uma API REST desenvolvida em .NET, conforme mencionado no link do repositório backend.

7. **Integração com Backend Desenvolvido em .NET (10 Pontos)**
   - Toda a comunicação de dados (CRUD) é feita através da API .NET com autenticação JWT, garantindo segurança e integridade dos dados.

8. **Tela de Login Integrada ao Backend (10 Pontos)**
   - O aplicativo possui uma tela de login (`LoginActivity`) que utiliza a API .NET para autenticação dos usuários.

---

## Instruções para Executar o Aplicativo no Android Studio

### Pré-requisitos

- **Android Studio** instalado em sua máquina (versão recomendada: Arctic Fox ou superior).
- **Emulador Android** configurado ou dispositivo físico com modo desenvolvedor ativado.
- **Conexão com a Internet** para baixar dependências e comunicar-se com o backend.

### Passos para Configuração

1. ### Clone o Repositório do Backend
   - Clone o repositório do backend em .NET:

     ```bash
     git clone https://github.com/rozyar/Dotnet-Global-Solution.git
     ```

   - Siga as instruções no repositório para configurar e iniciar o servidor backend. Certifique-se de que ele esteja rodando na porta **5086**, conforme configurado no aplicativo móvel.

2. ### Clone o Repositório do Aplicativo Móvel
   - Clone o repositório do aplicativo móvel (disponível em seu repositório local ou plataforma de versionamento).

3. ### Abra o Projeto no Android Studio
   - Abra o Android Studio.
   - Selecione **"Open an Existing Project"**.
   - Navegue até a pasta do projeto clonado e selecione-a.

4. ### Sincronize as Dependências
   - O Android Studio solicitará que você sincronize o projeto para baixar todas as dependências necessárias. Clique em **"Sync Now"** quando solicitado.

5. ### Configure o Emulador ou Dispositivo Físico
   - **Emulador**: Configure um emulador Android com a **API Level 24** ou superior.
   - **Dispositivo Físico**: Ative o modo desenvolvedor e depuração USB em seu dispositivo e conecte-o ao computador.

6. ### Compile e Execute o Aplicativo
   - Clique no botão **"Run"** (ícone de play) no Android Studio.
   - Selecione o emulador ou dispositivo físico onde deseja executar o aplicativo.
   - Aguarde a compilação e instalação do aplicativo.

---

## Funcionalidades do Aplicativo

1. **Tela de Login**
   - O usuário pode fazer login com suas credenciais cadastradas. A autenticação é realizada através da API .NET com JWT.

2. **Tela de Registro**
   - Permite que novos usuários se registrem fornecendo nome, e-mail e senha.

3. **Tela Principal (Home)**
   - Apresenta as principais funcionalidades do aplicativo:
     - Calcular Painéis Solares
     - Histórico de Análises
     - Gerenciar Eletrodomésticos
     - Logout

4. **Calcular Painéis Solares**
   - O usuário informa as horas de luz solar disponíveis por dia. O aplicativo calcula a quantidade de painéis solares necessários com base nos eletrodomésticos cadastrados.

5. **Gerenciar Eletrodomésticos**
   - O usuário pode adicionar novos eletrodomésticos, que serão utilizados nos cálculos. Lista os eletrodomésticos cadastrados, permitindo edição e exclusão.

6. **Histórico de Análises**
   - Exibe o histórico de análises realizadas pelo usuário. Cada item mostra detalhes como data, consumo total, horas de luz solar e o resultado do cálculo.

---

## Arquitetura do Projeto

- **MVVM (Model-View-ViewModel)**: O aplicativo segue uma arquitetura organizada, separando as responsabilidades em diferentes camadas.
- **Retrofit**: Utilizado para a comunicação com a API REST.
- **Kotlin Coroutines**: Para operações assíncronas sem bloquear a interface do usuário.
- **SharedPreferences (SessionManager)**: Gerencia o token de autenticação do usuário usando o `SharedPreferences` para armazenar informações de sessão localmente.

---

## Dependências Utilizadas

- **Retrofit 2**: Comunicação com APIs REST.
- **Gson Converter**: Conversão de objetos JSON.
- **OkHttp Logging Interceptor**: Logging das requisições HTTP.
- **Kotlin Coroutines**: Programação assíncrona.
- **Material Design Components**: Componentes de interface seguindo as diretrizes do Material Design.
- **RecyclerView**: Exibição de listas eficientes.
- **SharedPreferences**: Armazenamento de dados simples em key-value pairs.

---

## Considerações Finais

Este projeto demonstra a capacidade da equipe em desenvolver um aplicativo completo, integrando frontend em **Kotlin** com um backend robusto em **.NET**. Todas as funcionalidades requeridas foram implementadas, garantindo uma experiência de usuário fluida e intuitiva.

---

## Licença

Este projeto é licenciado sob os termos da licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## Contato

Para quaisquer dúvidas ou sugestões, entre em contato com um dos membros da equipe através de seus perfis no GitHub.
