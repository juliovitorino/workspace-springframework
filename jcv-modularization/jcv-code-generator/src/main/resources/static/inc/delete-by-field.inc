    @Modifying
    @Query(value = "DELETE FROM ${schemap}${tabela} WHERE ${campo} = :${dto}", nativeQuery = true)
    void deleteBy${Adto}(@Param(${classebase}Constantes.${CCdto}) ${tipodart} ${dto});
