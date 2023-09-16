# Configuração do Sonatype Nexus Repository Manager

## Repositório do tipo proxy

Faz com que o maven tente buscar as dependências localmente na pasta **.m2/repository**.
Se não forem encontrados as dependências, tentar-se-á buscar no na repositório
apontado pela url 

```text
localhost:8081/repository/<nome-do-repositorio>
```

e se não tiver a dependencia no Nexus
este por sua vez, entrara emm contato com o repositório maven oficial para buscar a 
dependencia do seu projeto.

```text
https://repo1.maven.org/maven2/
```

### Criando um novo repositório proxy
1. clique no icone de engrenagem
2. clique em repositories
3. clique em "+ Create Repository"
4. Escolha a opção maven2 (proxy)
5. Preencha o campo **Name** com o nome do seu proxy. ex: ms-maven-proxy
6. Campo version policy = Reelase
7. preencha o campo **Proxy Remote Storage** com a url https://repo1.maven.org/maven2/
8. Clique no botão **Create repository** para finalizar

## Repositório do tipo hosted
## Repositório do tipo group

