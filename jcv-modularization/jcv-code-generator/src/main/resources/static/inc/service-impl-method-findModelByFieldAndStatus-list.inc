
    @Override
    @Transactional(transactionManager="${projeto}transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = ${classebase}NotFoundException.class
    )
    public ${classebase}DTO find${classebase}By${Adto}AndStatus(${tipodart} ${dto}, String status) {
        Long maxId = ${lclassebase}Repository.loadMaxIdBy${Adto}AndStatus(${dto}, status);
        if(maxId == null) maxId = 0L;
        Optional<${classebase}> ${lclassebase}Data =
            Optional.ofNullable( ${lclassebase}Repository
                .findById(maxId)
                .orElseThrow(
                    () -> new ${classebase}NotFoundException("${classebase} não encontrada com id = " + ${dto},
                        HttpStatus.NOT_FOUND,
                        "${classebase} não encontrada com ${dto} = " + ${dto}))
                );
        return ${lclassebase}Data.isPresent() ? this.toDTO(${lclassebase}Data.get()) : null ;
    }

    @Override
    @Transactional(transactionManager="${projeto}transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class,
    noRollbackFor = ${classebase}NotFoundException.class
    )
    public ${classebase}DTO find${classebase}By${Adto}AndStatus(${tipodart} ${dto}) {
        return this.find${classebase}By${Adto}AndStatus(${dto}, GenericStatusEnums.ATIVO.getShortValue());
    }