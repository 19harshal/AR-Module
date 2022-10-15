package in.ashokit.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.entity.Plan;
import in.ashokit.props.AppProperties;
import in.ashokit.service.PlanService;

@RestController
public class PlanRestController {

	@Autowired
	private PlanService planService;

	@Autowired
	private AppProperties props;
	
	public PlanRestController(PlanService planService) {
		super();
		this.planService = planService;
	}

	@GetMapping("/categories")
	public ResponseEntity<Map<Integer,String>> planCategories(){
		
		Map<Integer,String> categories = planService.getPlanCategories();
		return new ResponseEntity<>(categories,HttpStatus.OK);
	}
	
	@PostMapping("/plan")
	public ResponseEntity<String> savePlan(@RequestBody Plan plan){
		String responseMsg = "";
		boolean isSave = planService.savePlan(plan);
		
		Map<String, String> message = props.getMessage();
		if(isSave) {
			responseMsg = message.get("planSave");
		}else {
			responseMsg = message.get("planFail");
		}
		return new ResponseEntity<>(responseMsg,HttpStatus.CREATED);
	}
	
	@GetMapping("plans")
	public ResponseEntity<List<Plan>> plans(){
		List<Plan> allPlans = planService.getAllPlans();
		return new ResponseEntity<>(allPlans,HttpStatus.OK);
		
	}
	
	@GetMapping("/plan/{planId}")
	public  ResponseEntity<Plan> editPlan(@PathVariable Integer planId){
		Plan plan = planService.getPlanById(planId);
		return new ResponseEntity<>(plan,HttpStatus.OK);
	}

	@PutMapping("plan")
	public ResponseEntity<String> updatePlan(@RequestBody Plan plan){
		boolean updatePlan = planService.updatePlan(plan);
		String msg = "";
		Map<String, String> message = props.getMessage();
		if ( updatePlan )
			msg = message.get("planUpdateSuccess");
		else
			msg = message.get("planUpdateFailed");
		return new ResponseEntity<>(msg,HttpStatus.OK);

	}
	
	
	@DeleteMapping("/plan/{planId}")
	public  ResponseEntity<String> deletePlan(@PathVariable Integer planId){
		boolean isDeleted = planService.deletePlanById(planId);
		String msg = "";
		if ( isDeleted)
			msg = "Plan Deleted";
		else
			msg = "Plan not Deleted";
		
		return new ResponseEntity<>(msg,HttpStatus.OK);
	}
	
	@PutMapping("/status-change/{planId}/{status}")
	public ResponseEntity<String> statusChange(@PathVariable Integer planId,@PathVariable String status){
		boolean isStatusChanged = planService.planStatusChange(planId, status);
		String msg="";
		if (isStatusChanged)	
			msg = "status changed";
		else
			msg = "status not changed";
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}
	
}
