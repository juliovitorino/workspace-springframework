    @Override
    @Transactional(transactionManager="${projeto}transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = ${classebase}NotFoundException.class
    )
    public List<${classebase}DTO> findAll${classebase}By${Adto}AndStatus(${tipodart} ${dto}, String status) {
        return ${lclassebase}Repository.findAllBy${Adto}AndStatus(${dto}, status).stream().map(this::toDTO).collect(Collectors.toList());
    }