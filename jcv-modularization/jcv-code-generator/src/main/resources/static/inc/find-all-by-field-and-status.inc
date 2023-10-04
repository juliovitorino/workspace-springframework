     @Query(value = "SELECT * FROM ${schemap}${tabela} WHERE ${campo} = :${dto} AND  IN_STATUS = :status ", nativeQuery = true)
     List<${classebase}> findAllBy${Adto}AndStatus(${tipodart} ${dto}, String status);
