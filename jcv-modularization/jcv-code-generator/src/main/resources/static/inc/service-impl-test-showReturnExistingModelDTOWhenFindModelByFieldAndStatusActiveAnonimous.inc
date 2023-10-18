
    @Test
    public void showReturnExisting${classebase}DTOWhenFind${classebase}By${Adto}AndStatusActiveAnonimous() {
        // scenario
        ${tipodart} ${dto}Mock = ${magic-content};
        Long maxIdMock = 1972L;
        Optional<${classebase}> ${lclassebase}ModelMock = Optional.ofNullable(${classebase}ModelBuilder.new${classebase}ModelTestBuilder()
                .${dto}(${dto}Mock)
                .now()
        );
        Mockito.when(${lclassebase}RepositoryMock.loadMaxIdBy${Adto}AndStatus(${dto}Mock, "A")).thenReturn(maxIdMock);
        Mockito.when(${lclassebase}RepositoryMock.findById(maxIdMock)).thenReturn(${lclassebase}ModelMock);

        // action
        ${classebase}DTO result = ${lclassebase}Service.find${classebase}By${Adto}AndStatus(${dto}Mock);

        // validate
        Assertions.assertEquals(${dto}Mock, result.get${Adto}());

    }
    @Test
    public void showReturn${classebase}NotFoundExceptionWhenNonExistenceFind${classebase}By${Adto}AndStatusActiveAnonimous() {
        // scenario
        ${tipodart} ${dto}Mock = ${magic-content};
        Long noMaxIdMock = 0L;
        Optional<${classebase}> ${lclassebase}ModelMock = Optional.empty();
        Mockito.when(${lclassebase}RepositoryMock.loadMaxIdBy${Adto}AndStatus(${dto}Mock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(${lclassebase}RepositoryMock.findById(noMaxIdMock)).thenReturn(${lclassebase}ModelMock);

        // action
        ${classebase}NotFoundException exception = Assertions.assertThrows(${classebase}NotFoundException.class,
                ()->${lclassebase}Service.find${classebase}By${Adto}AndStatus(${dto}Mock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(${Uclassebase}_NOTFOUND_WITH_${Udto}));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }