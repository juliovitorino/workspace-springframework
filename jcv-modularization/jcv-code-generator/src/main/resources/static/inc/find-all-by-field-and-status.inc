     @Query(value = "SELECT * FROM ${schemap}${tabela} WHERE ${campo} = :${dto} AND  ${campostatus} = :status ", nativeQuery = true)
     List<${classebase}> findAllBy${Adto}AndStatus(${tipodart} ${dto}, String status);
