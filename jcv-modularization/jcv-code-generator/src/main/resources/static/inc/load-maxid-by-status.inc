     @Query(value = "SELECT MAX(${pk}) AS maxid FROM ${schemap}${tabela} WHERE ${campo} = :${dto} AND IN_STATUS = :status ", nativeQuery = true)
     Long loadMaxIdBy${Adto}AndStatus(${tipodart} ${dto}, String status);
