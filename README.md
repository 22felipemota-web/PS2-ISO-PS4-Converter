# PS2 ISO → PS4 PKG Converter (Android 14/15)

Projeto-base nativo Android em Kotlin, com `minSdk 29`, `targetSdk 35` e `compileSdk 35`.

## Recursos implementados
- Interface escura em português.
- Seletor de arquivo via Storage Access Framework (ISO/BIN/IMG).
- Seletor de pasta de saída.
- Nome do jogo, perfil e região.
- Área de progresso e logs.
- Mensagem clara de que o pipeline de conversão ainda não está conectado.

## Abrir e compilar
1. Instale Android Studio atualizado e o JDK exigido pela versão do Android Gradle Plugin.
2. Abra a pasta do projeto no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Selecione um dispositivo/emulador Android 14 ou 15.
5. Use **Build > Build APK(s)**.

O APK é gerado pelo Android Studio em `app/build/outputs/apk/debug/`.

## Limitação essencial
Este projeto não converte realmente ISO de PS2 em PKG de PS4. A geração de PKG requer um pipeline compatível de preparação/emulação e ferramentas de empacotamento FPKG. O caminho mais realista é usar o app Android como interface/controle e executar o processamento pesado em um PC Windows, ou implementar um backend separado. Não renomeie a ISO para `.pkg`: isso não cria um pacote válido.

O projeto não distribui jogos, firmware, chaves ou ferramentas proprietárias.


## Compilar automaticamente na nuvem (sem instalar SDK no celular)
O repositório inclui `.github/workflows/build-apk.yml`. Para gerar o APK usando GitHub Actions:
1. Crie um repositório no GitHub e envie o conteúdo desta pasta.
2. Abra a aba **Actions** e execute **Build Android APK** (ou faça push para `main`).
3. Ao terminar, abra a execução e baixe o artefato `PS2-ISO-PS4-Converter-debug`.
4. Extraia o artefato; o arquivo é `app-debug.apk`.

A compilação na nuvem precisa de uma conta GitHub e de enviar o código para um repositório. Este ambiente não possui Android SDK/Gradle instalados, portanto não é possível afirmar que um APK já foi compilado aqui.
