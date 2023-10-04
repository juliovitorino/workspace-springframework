    @Query(value = "SELECT * FROM ${schemap}${tabela} WHERE ${pk} = (SELECT MAX(${pk}) AS maxid FROM ${schemap}${tabela} WHERE ${campo} = :${dto} AND  IN_STATUS = :status) ", nativeQuery = true)
    Optional<${classebase}> findBy${Adto}AndStatus(${tipodart} ${dto}, String status);
