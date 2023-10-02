    @Override
    @Transactional(
    transactionManager = "transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)
    public ${classebase}DTO update${Adto}ById(Long id, ${tipodart} ${dto}) {
        findById(id);
        ${lclassebase}Repository.update${Adto}ById(id, ${dto});
        return findById(id);
    }