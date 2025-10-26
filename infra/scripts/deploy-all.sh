#!/usr/bin/env bash
set -euo pipefail

REGION="${1:-ap-south-1}"

echo "Deploying Network..."
aws cloudformation deploy --region "$REGION" \
  --stack-name sb-net \
  --template-file infra/cloudformation/00-network.yaml \
  --capabilities CAPABILITY_NAMED_IAM

echo "Deploying ECR..."
aws cloudformation deploy --region "$REGION" \
  --stack-name sb-ecr \
  --template-file infra/cloudformation/01-ecr.yaml \
  --capabilities CAPABILITY_NAMED_IAM

echo "Deploying EKS..."
aws cloudformation deploy --region "$REGION" \
  --stack-name sb-eks \
  --template-file infra/cloudformation/02-eks.yaml \
  --capabilities CAPABILITY_NAMED_IAM

echo "Deploying Data..."
aws cloudformation deploy --region "$REGION" \
  --stack-name sb-data \
  --template-file infra/cloudformation/03-data.yaml \
  --capabilities CAPABILITY_NAMED_IAM

echo "Deploying Cognito..."
aws cloudformation deploy --region "$REGION" \
  --stack-name sb-cognito \
  --template-file infra/cloudformation/04-cognito.yaml \
  --parameter-overrides DomainPrefix="skillbridge-$RANDOM" \
  --capabilities CAPABILITY_NAMED_IAM

echo "NOTE: Provision your EKS Ingress/Gateway (Service type LoadBalancer with NLB)."
echo "Retrieve its NLB ARN and then deploy API Gateway with that ARN."

# Example to get NLB ARN (if annotated service exposes it in description/ELB tag):
# NLB_ARN=$(aws elbv2 describe-load-balancers --names <your-nlb-name> --query "LoadBalancers[0].LoadBalancerArn" --output text --region "$REGION")

# echo "Deploying API Gateway..."
# aws cloudformation deploy --region "$REGION" \
#   --stack-name sb-apigw \
#   --template-file infra/cloudformation/05-apigw.yaml \
#   --parameter-overrides NlbArn="$NLB_ARN" \
#   --capabilities CAPABILITY_NAMED_IAM
