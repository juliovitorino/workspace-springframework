    @Test
    public void shouldReturn${classebase}DTOWhenUpdateExisting${Adto}ById() {
        // scenario
        ${tipodart} ${dto}UpdateMock = ${magic-content};
        Optional<${classebase}> ${lclassebase}ModelMock = Optional.ofNullable(${classebase}ModelBuilder.new${classebase}ModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(${lclassebase}RepositoryMock.findById(420L)).thenReturn(${lclassebase}ModelMock);
        Mockito.doNothing().when(${lclassebase}RepositoryMock).update${Adto}ById(420L, ${dto}UpdateMock);

        // action
        ${lclassebase}Service.update${Adto}ById(420L, ${dto}UpdateMock);

        // validate
        Mockito.verify(${lclassebase}RepositoryMock,Mockito.times(1)).update${Adto}ById(420L, ${dto}UpdateMock);
    }