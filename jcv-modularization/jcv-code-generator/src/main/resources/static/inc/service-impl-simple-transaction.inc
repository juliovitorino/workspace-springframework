    @Transactional(
    transactionManager = "${projeto}transactionManager",
    propagation = Propagation.REQUIRED,
    rollbackFor = Throwable.class)