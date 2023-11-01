    @Test
    public void shouldReturnExistent${classebase}DTOWhenFind${classebase}By${Adto}AndStatus() {
        // scenario
        Optional<${classebase}> ${lclassebase}ModelMock = Optional.ofNullable(${classebase}ModelBuilder.new${classebase}ModelTestBuilder().now());
        Mockito.when(${lclassebase}RepositoryMock.loadMaxIdBy${Adto}AndStatus(${magic-content}, "A")).thenReturn(1L);
        Mockito.when(${lclassebase}RepositoryMock.findById(1L)).thenReturn(${lclassebase}ModelMock);

        // action
        ${classebase}DTO result = ${lclassebase}Service.find${classebase}By${Adto}AndStatus(${magic-content}, "A");

        // validate
        Assertions.assertInstanceOf(${classebase}DTO.class,result);
    }
    @Test
    public void shouldReturn${classebase}NotFoundExceptionWhenNonExistence${classebase}${Adto}AndStatus() {
        // scenario
        Mockito.when(${lclassebase}RepositoryMock.loadMaxIdBy${Adto}AndStatus(${magic-content}, "A")).thenReturn(0L);
        Mockito.when(${lclassebase}RepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        ${classebase}NotFoundException exception = Assertions.assertThrows(${classebase}NotFoundException.class,
                ()->${lclassebase}Service.find${classebase}By${Adto}AndStatus(${magic-content}, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(${Uclassebase}_NOTFOUND_WITH_${Udto}));
    }