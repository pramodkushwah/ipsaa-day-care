package com.synlabs.ipsaa.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.center.City;
import com.synlabs.ipsaa.entity.center.State;
import com.synlabs.ipsaa.entity.center.Zone;
import com.synlabs.ipsaa.entity.common.Address;
import com.synlabs.ipsaa.entity.common.LegalEntity;
import com.synlabs.ipsaa.entity.common.Role;
import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.entity.staff.QEmployee;
import com.synlabs.ipsaa.entity.student.QStudent;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.enums.AddressType;
import com.synlabs.ipsaa.enums.ApprovalStatus;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.ftl.model.ApprovalModel;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.view.center.*;
import com.synlabs.ipsaa.view.common.UserRequest;
import com.synlabs.ipsaa.view.common.UserResponse;
import org.apache.poi.util.StringUtil;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CenterService extends BaseService
{

  @Autowired
  private CenterRepository centerRepository;

  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private ZoneRepository zoneRepository;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private LegalEntityRepository legalEntityRepository;

  @Autowired
  private StaffService staffService;

  @Autowired
  private EmployeeService employeeService;
  @Autowired
  private StudentService studentService;
  @Autowired
  UserService userService;
  @Autowired
  FeeService centerFeeService;

  @Autowired
  StateRepository stateRepository;

  public List<LegalEntity> listEntities() {
    return legalEntityRepository.findAll();
  }

  public List<Center> list(CenterListRequest request)
  {

    List<Center> usercenters = getUserCenters();

    if (!StringUtils.isEmpty(request.getCity()))
    {
      return usercenters.stream().filter(c -> c.getAddress().getCity().equals(request.getCity())).collect(Collectors.toList());
    }

    if (!StringUtils.isEmpty(request.getZone()))
    {
      return usercenters.stream().filter(c -> c.getZone().getName().equals(request.getZone())).collect(Collectors.toList());
    }

    return usercenters;
  }

  public List<Center> listAll()
  {
    return centerRepository.findAll();
  }

  public Center createCenter(CenterRequest request)
  {
    Center center = request.toEntity();

    if (centerRepository.countByCode(center.getCode()) != 0)
    {
      throw new ValidationException("There is already another center with same code!");
    }

    City city = cityRepository.findOneByName(request.getCity());
    if (city != null)
    {
      center.getAddress().setCity(request.getCity());
    }

    Zone zone = zoneRepository.findOneByName(request.getZone());
    center.setZone(zone);

    State state= stateRepository.findOneByName(request.getState());////////Avneet
    if(state!= null){
      center.getAddress().setState(request.getState());
    }

    center.setActive(true);
    return centerRepository.saveAndFlush(center);
  }

  public void deleteCenter(CenterRequest request)
  {
    if (request.getId() == null)
    {
      throw new ValidationException("Center Id is required.");
    }
    Center center = centerRepository.findOne(request.getId());
    if (center == null)
    {
      throw new ValidationException(String.format("Cannot locate center[id=%s]",request.getMaskId()));
    }
   // System.out.println("employes "+employeeService.findEmployeeByCenter(center) +" "+request.getId());
   // System.out.println("em");
   if(employeeService.findEmployeeByCenter(center)!=null && employeeService.findEmployeeByCenter(center).size()>0){

     throw new ValidationException(String.format("Cannot delete this center ! employee detected",request.getMaskId()));

    }else if(studentService.getStudentByCenterId(center)!=null && studentService.getStudentByCenterId(center).size()>0){

     throw new ValidationException(String.format("Cannot delete this center ! student detected",request.getMaskId()));

   }
   else if(centerFeeService.centerProgramFeeRepository(center.getId())!=null){
     throw new ValidationException(String.format("Cannot delete this center ! Center Fee Program detected",request.getMaskId()));
   }
   else{
     center.setActive(false);
     centerRepository.saveAndFlush(center);
   }
  }
  public Center updateCenter(CenterRequest request)
  {
    Center center = centerRepository.getOne(request.getId());
    if (center == null)
    {
      throw new NotFoundException("Missing center");
    }
    center = request.toEntity(center);

    City city = cityRepository.findOneByName(request.getCity());
    if (city != null)
    {
      center.getAddress().setCity(request.getCity());
    }



    Address address = center.getAddress();
    address.setAddress(request.getAddress());
    address.setAddressType(AddressType.Center);
    //address.setState(request.getState());
    address.setZipcode(request.getZipcode());
    address.setPhone(request.getPhone());

    State state= stateRepository.findOneByName(request.getState());
    if(state !=null){
      center.getAddress().setState(state.getName());
    }else{
      center.getAddress().setState(city.getState().getName());
    }

    Zone zone = zoneRepository.findOneByName(request.getZone());
    center.setZone(zone);
    centerRepository.saveAndFlush(center);
    return center;
  }

  public List<City> listCities(CityRequest request)
  {

    if (!StringUtils.isEmpty(request.getZone()))
    {
      return cityRepository.findByZoneName(request.getZone());
    }

    return cityRepository.findAll();
  }

  public City saveCity(CityRequest request)
  {
    Zone zone = zoneRepository.findOneByName(request.getZone());

    if (zone == null)
    {
      throw new NotFoundException(String.format("Cannot locate zone %s", request.getZone()));
    }

    /////Avneet
    State state=stateRepository.findOneByName(request.getState());
    if(state == null){
      throw new NotFoundException(String.format("Cannot locate state %s",request.getState()));
    }

    if (cityRepository.countByName(request.getName()) > 0)
    {
      throw new ValidationException(String.format("City with same name [%s] already exists!", request.getName()));
    }

    City city = request.toEntity();
    city.setZone(zone);
    city.setState(state);
    city = cityRepository.saveAndFlush(city);
    return city;
  }

  public void deleteCity(CityRequest request)
  {
    cityRepository.delete(request.getId());
  }

  public City getCity(CityRequest request)
  {
    return cityRepository.findOne(request.getId());
  }

  public City updateCity(CityRequest cityRequest)
  {
    City city = this.cityRepository.findOne(cityRequest.getId());
    if (city == null)
    {
      throw new NotFoundException("City not Found of id " + cityRequest.getMaskedId());
    }

    Zone zone = this.zoneRepository.findOneByName(cityRequest.getZone());
    if (zone == null)
    {
      throw new NotFoundException("Zone not Found of name " + cityRequest.getZone());
    }

    State state= stateRepository.findOneByName(cityRequest.getState());
    if(state == null){
      throw new NotFoundException("State not found of Name" +cityRequest.getState());
    }

    city.setName(cityRequest.getName());
    city.setZone(zone);
    city.setState(state);

    return cityRepository.saveAndFlush(city);
  }

  public List<Zone> listZones()
  {
    return zoneRepository.findAll();
  }

  public Zone saveZone(ZoneRequest request)
  {

    if (StringUtils.isEmpty(request.getName()))
    {
      throw new ValidationException("Missing name");
    }

    if (zoneRepository.countByName(request.getName()) > 0)
    {
      throw new ValidationException(String.format("Zone with name [%s] already exists", request.getName()));
    }

    return zoneRepository.saveAndFlush(request.toEntity());
  }

  public Zone updateZone(ZoneRequest request)
  {

    Zone zone = zoneRepository.findOne(request.getId());
    if (zone == null)
    {
      throw new NotFoundException("Cannot locate zone");
    }

    zone.setName(request.getName());
    return this.zoneRepository.saveAndFlush(zone);
  }

  public void deleteZone(ZoneRequest request)
  {

    Zone zone = zoneRepository.findOne(request.getId());

    if (zone == null)
    {
      throw new NotFoundException("Zone not found with id " + request.getMaskedId());
    }

    zoneRepository.delete(request.getId());
  }

  public List<ApprovalModel> getPendingApprovals()
  {
    List<ApprovalModel> approvals = new ArrayList<>();
    Map<String, ApprovalModel> approvalModelMap = new HashMap<>();

    JPAQuery<Student> query = new JPAQuery<>(entityManager);
    QStudent student = QStudent.student;
    QEmployee employee = QEmployee.employee;

    List<Tuple> fetch = query.select(student.center, student.countDistinct()).from(student)
                             .where(
                                 student.approvalStatus.eq(ApprovalStatus.NewApproval)
                                                       .and(student.active.eq(true))
                                   )
                             .groupBy(student.center)
                             .fetch();

    for (Tuple row : fetch)
    {
      String centerName = row.get(student.center).getName();
      ApprovalModel approvalModel = new ApprovalModel(centerName);
      approvalModel.setStudentApprovalCount(row.get(student.countDistinct()).intValue());
      approvalModelMap.put(centerName, approvalModel);
    }

    fetch = query.select(employee.costCenter, employee.countDistinct()).from(employee)
                 .where(
                     employee.approvalStatus.eq(ApprovalStatus.NewApproval)
                                            .and(employee.active.eq(true))
                       )
                 .groupBy(employee.costCenter)
                 .fetch();

    for (Tuple row : fetch)
    {
      String centerName = row.get(employee.costCenter).getName();
      ApprovalModel approvalModel = approvalModelMap.get(centerName) == null ? new ApprovalModel(centerName) : approvalModelMap.get(centerName);
      approvalModel.setStaffApprovalCount(row.get(employee.countDistinct()).intValue());
    }

    Set<String> keys = approvalModelMap.keySet();
    for (String key : keys)
    {
      ApprovalModel approval = approvalModelMap.get(key);
      if (approval.getStaffApprovalCount() > 0 || approval.getStudentApprovalCount() > 0)
      {
        approvals.add(approval);
      }
    }
    return approvals;
  }

  public List<ApprovalCountResponse> getStudentApprovalCount()
  {
//    1. put all user center in map with approval count 0
    Map<Center, Long> centerMap = new HashMap<>();
    for (Center center : getUserCenters())
    {
      centerMap.put(center, 0l);
    }

//    2. get all center with student approval count
    QStudent student = QStudent.student;
    JPAQuery<Student> query = new JPAQuery<>(entityManager);
    List<Tuple> fetch = query.select(student.center, student.count())
                             .from(student)
                             .where(student.active.isTrue())
                             .where(student.approvalStatus.eq(ApprovalStatus.NewApproval)
                                     .or(student.approvalStatus.eq(ApprovalStatus.Pending)))
                             .groupBy(student.center)
                             .fetch();
//    3. update approval count in map
    for (Tuple tuple : fetch)
    {
      Long count = centerMap.get(tuple.get(student.center));
      if (count != null)
      {
        centerMap.put(tuple.get(student.center), tuple.get(student.count()));
      }
    }

//    4. put map values to response list
    List<ApprovalCountResponse> list = new ArrayList<>();
    for (Center center : centerMap.keySet())
    {
      list.add(new ApprovalCountResponse(center, centerMap.get(center).intValue()));
    }
    return list;
  }

  public List<ApprovalCountResponse> getStaffApprovalCount()
  {
//        1. put all user center in map with approval count 0
    Map<Center, Long> centerMap = new HashMap<>();
    for (Center center : getUserCenters())
    {
      centerMap.put(center, 0l);
    }

//        2. get all center with staff approval count
    QEmployee employee = QEmployee.employee;
    JPAQuery<Student> query = new JPAQuery<>(entityManager);
    List<Tuple> fetch = query.select(employee.costCenter, employee.count())
                             .from(employee)
                             .where(employee.active.isTrue())
                             .where(employee.approvalStatus.eq(ApprovalStatus.NewApproval))
                             .groupBy(employee.costCenter)
                             .fetch();

//        3.update approval count in map
    for (Tuple tuple : fetch)
    {
      Long count = centerMap.get(tuple.get(employee.costCenter));
      if (count != null)
      {
        centerMap.put(tuple.get(employee.costCenter), tuple.get(employee.count()));
      }
    }

//        4. put map values to response list
    List<ApprovalCountResponse> list = new ArrayList<>();
    for (Center center : centerMap.keySet())
    {
      list.add(new ApprovalCountResponse(center, centerMap.get(center).intValue()));
    }
    return list;
  }


  ///////////////////Avneet

  //returns all states
  public List<State> listOfStates(){
    return stateRepository.findAll();
  }

  ///Save the new state
  public State saveState(StateRequest request){

    if(StringUtils.isEmpty(request.getName())){
      throw new ValidationException("State name is missing");
    }

    if (stateRepository.countByName(request.getName() )  >0){
        throw new ValidationException(String.format("State [%s] already exists",request.getName()));
    }

    return stateRepository.saveAndFlush(request.toEntity());
  }

  ///Update State
  public State updateState(StateRequest stateRequest){

    State state=stateRepository.findOne(stateRequest.getId());

    if(state == null){
      throw new NotFoundException("Cannot Locate state");
    }

    state.setName(stateRequest.getName().toUpperCase());
    return stateRepository.saveAndFlush(state);
  }


  ///State by City
  public State getState(Long id){

    City city= cityRepository.findOne(unmask(id));    //check city

    if(city == null){
      throw new ValidationException("Missing City");
    }
    State s=city.getState();
    return s;
  }

  public void deleteState(StateRequest request){

    State state= stateRepository.findOne(request.getId());

    List<City> city= cityRepository.findByState(state);

    if(!city.isEmpty()){
      throw  new ValidationException("Cannot delete this state");
    }

    stateRepository.delete(request.getId());
  }

  //////////////Avneet
   public List<State> getStateByZone(Long zoneId){

      Zone zone= zoneRepository.findOne(unmask(zoneId));
      if(zone == null)
          throw new ValidationException(String.format("Zone with this %s id doesn't exist.",zoneId));

      List<State> states= stateRepository.findByCitiesZoneId(unmask(zoneId));
      return states.stream().distinct().collect(Collectors.toList());
  }

  public List<City> getCityByState(Long stateId){

      State state = stateRepository.findOne(unmask(stateId));
      if(state == null)
          throw new ValidationException(String.format("State with this %s id doesn't exist.",stateId));

      return cityRepository.findByState(state);
  }
}
