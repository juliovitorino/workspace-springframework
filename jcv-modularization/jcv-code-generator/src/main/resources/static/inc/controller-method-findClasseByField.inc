    @ApiResponses({
            @ApiResponse(code = 204, message = "Indica que o processo ${classebase} foi executado com sucesso"),
            @ApiResponse(code = 200, message = "Indica que o processo ${classebase} foi executado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorreu algum problema inesperado"),
    })
    @GetMapping(params = "${dto}")
    public ResponseEntity<${classebase}DTO> find${classebase}By${Adto}(@RequestParam(${classebase}Constantes.${CCdto}) ${tipodart} ${dto}) {
        try{
            ${classebase}DTO ${lclassebase}DTO = ${lclassebase}Service.find${classebase}By${Adto}AndStatus(${dto}, GenericStatusEnums.ATIVO.getShortValue());
            return Objects.nonNull(${lclassebase}DTO)
                ? new ResponseEntity<>(${lclassebase}DTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (${classebase}NotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(CommoditieBaseException e) {
            return new ResponseEntity(e.getMensagemResponse(), e.getHttpStatus());
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
