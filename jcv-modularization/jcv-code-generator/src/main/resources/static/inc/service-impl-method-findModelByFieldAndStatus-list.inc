
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
                    () -> new ${classebase}NotFoundException(${Uclassebase}_NOTFOUND_WITH_${Udto} + ${dto},
                        HttpStatus.NOT_FOUND,
                        ${Uclassebase}_NOTFOUND_WITH_${Udto} + ${dto}))
                );
        return ${lclassebase}Data.map(this::toDTO).orElse(null);
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