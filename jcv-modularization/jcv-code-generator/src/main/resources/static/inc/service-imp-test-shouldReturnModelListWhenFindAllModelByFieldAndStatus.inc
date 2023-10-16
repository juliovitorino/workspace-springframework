    @Test
    public void shouldReturn${classebase}ListWhenFindAll${classebase}By${Adto}AndStatus() {
        // scenario
        List<${classebase}> ${lclassebase}s = Arrays.asList(
            ${classebase}ModelBuilder.new${classebase}ModelTestBuilder().now(),
            ${classebase}ModelBuilder.new${classebase}ModelTestBuilder().now(),
            ${classebase}ModelBuilder.new${classebase}ModelTestBuilder().now()
        );

        Mockito.when(${lclassebase}RepositoryMock.findAllBy${Adto}AndStatus(${magic-content}, "A")).thenReturn(${lclassebase}s);

        // action
        List<${classebase}DTO> result = ${lclassebase}Service.findAll${classebase}By${Adto}AndStatus(${magic-content}, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }