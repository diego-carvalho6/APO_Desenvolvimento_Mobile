# Weather Forecast App

Este é um aplicativo Android de previsão do tempo, desenvolvido com Kotlin e Jetpack Compose. Ele oferece informações sobre o clima em tempo real para qualquer cidade, com funcionalidade para busca por nome de cidade ou escaneamento de QR Code.

## Funcionalidades

- **Previsão do Tempo**: Exibe a previsão do tempo atual e para os próximos dias.
- **Busca por Cidade**: Permite ao usuário buscar a previsão para qualquer cidade inserindo o nome no campo de texto.
- **QR Code**: O usuário pode escanear um QR code que contém o nome da cidade para obter a previsão do tempo de forma prática.
- **Mapa de Localização**: Mostra um mapa da localização da cidade, utilizando coordenadas geográficas obtidas via OpenStreetMap.

## Tecnologias Utilizadas

- **Kotlin**: Linguagem de programação principal para o desenvolvimento.
- **Jetpack Compose**: Para criação da UI de forma declarativa.
- **OpenWeatherMap API**: Para obtenção de dados sobre o clima.
- **ZXing Library**: Usada para escanear QR Codes.
- **OpenStreetMap API**: Para obter coordenadas geográficas das cidades e exibir o mapa.
- **OkHttp**: Para fazer as requisições HTTP.
- **Gson**: Para conversão de JSON.

## Como Rodar o Projeto

### Pré-requisitos

- Android Studio instalado.
- JDK 11 ou superior instalado.
- Um dispositivo Android ou emulador configurado.

### Passos para Executar

1. Clone o repositório:

    ```bash
    git clone https://github.com/seu-usuario/weather-forecast-app.git
    ```

2. Abra o projeto no Android Studio.

3. Sincronize as dependências do Gradle clicando em **Sync Now** na barra de notificações do Android Studio.

4. Configure seu emulador Android ou conecte um dispositivo físico.

5. Compile e execute o aplicativo no emulador ou dispositivo conectado.

### Permissões

O aplicativo requer permissões para acessar a câmera (para escaneamento de QR Code) e a localização (para mostrar a localização no mapa).

As permissões são solicitadas automaticamente durante a execução do aplicativo.

## Estrutura do Projeto

- **MainActivity**: A atividade principal que exibe a previsão do tempo e permite que o usuário insira o nome da cidade ou escaneie um QR code.
- **MapScreen**: Tela que exibe um mapa da cidade com as coordenadas obtidas via OpenStreetMap.
- **WeatherData**: Componente que exibe as informações detalhadas da previsão do tempo.
- **QR Code Scan**: Funcionalidade para escanear QR Codes com a biblioteca ZXing.

## APIs Utilizadas

### hqbrasil API

- Utilizada para buscar a previsão do tempo a partir do nome de uma cidade.

- **Endpoint**: `https://api.hgbrasil.com/weather`
- **Parâmetros**:
    - `key`: Sua chave de API (substitua com a sua).
    - `city_name`: Nome da cidade para obter a previsão.

### OpenStreetMap API

- Utilizada para renderizar o mapa.


- **Endpoint**: `https://nominatim.openstreetmap.org/search`
- **Parâmetros**:
    - `q`: Nome da cidade.
    - `format`: O formato da resposta (JSON).
    - `addressdetails`: Definido como 1 para obter detalhes do endereço.

### ZXing Library

- Utilizada para escanear QR codes e obter informações, como o nome de uma cidade para buscar a previsão do tempo.

## Contribuições

Se você quiser contribuir para o projeto, siga os passos abaixo:

1. Fork este repositório.
2. Crie uma branch para a sua funcionalidade (`git checkout -b feature/nova-funcionalidade`).
3. Faça as alterações necessárias e commite (`git commit -am 'Adiciona nova funcionalidade'`).
4. Envie para o repositório remoto (`git push origin feature/nova-funcionalidade`).
5. Crie um pull request.

## Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para mais detalhes.
